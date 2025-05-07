package org.sebi;


import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.*;
import io.fabric8.kubernetes.client.KubernetesClient;

import jakarta.inject.Inject;

import java.util.Map;

@ControllerConfiguration
public class DevoxxUkReconcilier implements Reconciler<DevoxxUk> {

    @Inject
    KubernetesClient client;

    @Override
    public UpdateControl<DevoxxUk> reconcile(DevoxxUk resource, Context<DevoxxUk> context) {
        String name = resource.getMetadata().getName();
        String namespace = resource.getMetadata().getNamespace();
        DevoxxUkSpec spec = resource.getSpec();

        // Labels
        Map<String, String> labels = Map.of("app", name);

        // Deployment
        Deployment deployment = new DeploymentBuilder()
            .withNewMetadata().withName(name).withNamespace(namespace).endMetadata()
            .withNewSpec()
                .withReplicas(spec.replicas)
                .withNewSelector().withMatchLabels(labels).endSelector()
                .withNewTemplate()
                    .withNewMetadata().withLabels(labels).endMetadata()
                    .withNewSpec()
                        .addNewContainer()
                            .withName("app")
                            .withImage(spec.image)
                            .addNewEnv()
                                .withName("GREETING")
                                .withValue(spec.greeting)
                            .endEnv()
                            .addNewPort()
                                .withContainerPort(8080)
                            .endPort()
                        .endContainer()
                    .endSpec()
                .endTemplate()
            .endSpec()
            .build();

        client.resource(deployment).inNamespace(namespace).createOrReplace();

        // Service
        Service service = new ServiceBuilder()
            .withNewMetadata().withName(name).withNamespace(namespace).endMetadata()
            .withNewSpec()
                .withSelector(labels)
                .addNewPort()
                    .withProtocol("TCP")
                    .withPort(80)
                    .withTargetPort(new IntOrString(8080))
                .endPort()
            .endSpec()
            .build();

        client.resource(service).inNamespace(namespace).createOrReplace();

        return UpdateControl.noUpdate();
    }
}
