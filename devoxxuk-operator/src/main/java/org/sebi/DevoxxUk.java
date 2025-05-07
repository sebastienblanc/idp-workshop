package org.sebi;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Singular;
import io.fabric8.kubernetes.model.annotation.Version;

@Version("v1alpha1")
@Group("sebi.org")
@Singular("devoxxuk")
public class DevoxxUk  extends CustomResource<DevoxxUkSpec, DevoxxUKStatus> implements Namespaced {
}
