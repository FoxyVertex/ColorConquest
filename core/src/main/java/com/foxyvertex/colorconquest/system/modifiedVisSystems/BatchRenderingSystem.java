package com.foxyvertex.colorconquest.system.modifiedVisSystems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.kotcrab.vis.runtime.component.Layer;
import com.kotcrab.vis.runtime.component.Renderable;
import com.kotcrab.vis.runtime.component.Shader;
import com.kotcrab.vis.runtime.component.Tint;
import com.kotcrab.vis.runtime.scene.LayerCordsSystem;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.LayerManager;
import com.kotcrab.vis.runtime.system.delegate.EntityProcessAgent;
import com.kotcrab.vis.runtime.system.delegate.EntityProcessPrincipal;
import com.kotcrab.vis.runtime.system.render.RenderBatchingSystem;
import com.kotcrab.vis.runtime.util.BagUtils;

/**
 * Created by aidan on 3/5/2017.
 */

public class BatchRenderingSystem extends BaseSystem implements EntityProcessPrincipal {
    private CameraManager cameraManager;
    private LayerManager layerManager;

    private ComponentMapper<Layer> layerCm;
    private ComponentMapper<Renderable> renderableCm;
    private ComponentMapper<Shader> shaderCm;
    private ComponentMapper<Tint> tintCm;

    private boolean sortedDirty = false;
    private final Bag<BatchRenderingSystem.Job> sortedJobs = new Bag<BatchRenderingSystem.Job>();

    private Batch batch;
    private boolean usingFromEditor;

    public BatchRenderingSystem (Batch batch, boolean usingFromEditor) {
        this.batch = batch;
        this.usingFromEditor = usingFromEditor;
    }

    /**
     * Declare entity relevant for agent.
     * <p>
     * After this is called, the principal can use the agent
     * interface to begin/end and process the given entity.
     * @param entityId entityId to process
     * @param agent interface to dispatch with.
     */
    @Override
    public void registerAgent (int entityId, EntityProcessAgent agent) {
        if (!renderableCm.has(entityId))
            throw new RuntimeException("RenderBatchingSystem requires agents entities to have component Renderable.");
        // register new job. this will influence sorting order.
        sortedJobs.add(new BatchRenderingSystem.Job(entityId, agent));
        sortedDirty = true;
    }

    /**
     * Revoke relevancy of entity for agent.
     * <p>
     * After this is called, the principal should no longer
     * attempt to process the entity with the agent.
     * @param entityId entityId to process
     * @param agent interface to dispatch with.
     */
    @Override
    public void unregisterAgent (int entityId, EntityProcessAgent agent) {
        // forget about the job.
        final Object[] data = sortedJobs.getData();
        for (int i = 0, s = sortedJobs.size(); i < s; i++) {
            final RenderBatchingSystem.Job e2 = (RenderBatchingSystem.Job) data[i];
            if (e2.entityId == entityId && e2.agent == agent) {
                sortedJobs.remove(i);
                sortedDirty = true;
                break;
            }
        }
    }

    @Override
    protected void processSystem () {
        cameraManager.getCamera().update();
        cameraManager.getUiCamera().update();

        LayerCordsSystem activeCordsSystem = LayerCordsSystem.WORLD;
        batch.setProjectionMatrix(cameraManager.getCombined());

        if (usingFromEditor == false) batch.begin();

        if (sortedDirty) {
            sortedDirty = false;
            BagUtils.sort(sortedJobs);
        }

        // iterate through all the jobs.
        EntityProcessAgent activeAgent = null;
        final Object[] data = sortedJobs.getData();
        for (int i = 0, s = sortedJobs.size(); i < s; i++) {
            final RenderBatchingSystem.Job job = (RenderBatchingSystem.Job) data[i];
            final EntityProcessAgent agent = job.agent;

            boolean changedBatchState = false;
            final boolean shaderUsed = shaderCm.has(job.entityId);
            LayerCordsSystem cordsSystem = null;

            final boolean tintUsed = tintCm.has(job.entityId);

            if (usingFromEditor == false) {
                cordsSystem = layerManager.getData(layerCm.get(job.entityId).layerId).cordsSystem;
            }

            // agent changed? end() the last agent, and begin() the next agent.
            if (agent != activeAgent) {
                if (activeAgent != null) {
                    activeAgent.end();
                }
                activeAgent = agent;
                activeAgent.begin();
            }

            if (shaderUsed) {
                changedBatchState = true;
                batch.end();
                batch.setShader(shaderCm.get(job.entityId).shader);
            }

            if (usingFromEditor == false && cordsSystem != activeCordsSystem) {
                activeCordsSystem = cordsSystem;

                switch (activeCordsSystem) {
                    case WORLD:
                        cameraManager.getViewport().apply();
                        batch.setProjectionMatrix(cameraManager.getCombined());
                        break;
                    case SCREEN:
                        cameraManager.getUiViewport().apply();
                        batch.setProjectionMatrix(cameraManager.getUiCombined());
                        break;
                }
            }

            if (tintUsed) {
                batch.setColor(tintCm.get(job.entityId).getTint());
            } else {
                batch.setColor(Color.WHITE);
            }

            if (changedBatchState) batch.begin();

            agent.process(job.entityId);

            if (shaderUsed) batch.setShader(null);
        }

        // finished, terminate final agent.
        if (activeAgent != null) {
            activeAgent.end();
        }

        if (usingFromEditor == false) batch.end();
    }

    public Batch getBatch () {
        return batch;
    }

    public void markDirty () {
        sortedDirty = true;
    }

    public boolean isDirty () {
        return sortedDirty;
    }

    /** Rendering job wrapper. */
    public class Job implements Comparable<RenderBatchingSystem.Job> {
        public final int entityId;
        public final EntityProcessAgent agent;

        /**
         * @param entityId entity we will process
         * @param agent agent responsible for processing.
         */
        public Job (final int entityId, final EntityProcessAgent agent) {
            this.entityId = entityId;
            this.agent = agent;
        }

        @Override
        public int compareTo (RenderBatchingSystem.Job o) {
            int layerResult = (int) Math.signum(layerCm.get(this.entityId).layerId - layerCm.get(o.entityId).layerId);

            if (layerResult == 0)
                return (int) Math.signum(renderableCm.get(this.entityId).zIndex - renderableCm.get(o.entityId).zIndex);
            else
                return layerResult;
        }
    }
}