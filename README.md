# Building your Internal Developer Platform in 90 minutes

## Pre-requisites

* Access to a Kubernetes CLuster (local like Kind or [Minikube](https://minikube.sigs.k8s.io/docs/start/?arch=%2Fmacos%2Farm64%2Fstable%2Fbinary+download) is okay).
* Github account : fork and clone this [repo](https://github.com/sebastienblanc/idp-workshop)

## Deploying a workload on Kubernetes

Go to the `devoxxuk-hello` folder and apply the k8s manifest : 

`kubectl apply -f manifest.yml` 

Check that the app get deployed, try to access the service. 

If you are using Minkube you can expose the service like this : `minikube service devoxxuk-hello`

Update the environment variable in the manifest and apply again. 

Make sure to delete your deployments before continuing the next steps. 

## Installing and using ArgoCD to introduce GitOps on your IDP

Follow those [instructions](https://argo-cd.readthedocs.io/en/stable/getting_started/) 

Create an ArgoCD App and point it to the `manifests` folder of you Git Forked repo. 

Commit & push the manifest file from the previous step (`devoxxuk-hello/manifest.yml`) inside the `manifests` folder. 

Make sure ArgoCD picks it up and apply the resource on your cluster. 

## Abstracting complexity by using the Operator Pattern

Let's deploy a custom operator that abstracts away the complexity of the previous deployment. 

First apply the CRD : 
`kubectl apply -f devoxxuk-operator/manifests/devoxxuks.sebi.org-v1.yml`

Then deploy the controller of the operator : 

`kubectl apply -f devoxxuk-operator/manifests/kubernetes.yml`

Now take a look at the custom resource : `devoxxuk-operator/manifests/myCR.yml` , modify if you want it. 

Delete the existing manifest.yml and commit & push it to root `manifests` folder of the repo. 

Make sure ArgoCD picks it up and apply the custom resource on your cluster. 

## Installing an Vendor Operator

We are going to install now an existing operator created by Zalando to deploy Postgres instances on your Kubernetes cluster. 

Clone this [repo](https://github.com/zalando/postgres-operator/tree/master).

Follow those [instructions](https://github.com/zalando/postgres-operator/blob/master/docs/quickstart.md#quickstart).

Create a Postgres by following those [intructions](https://github.com/zalando/postgres-operator/blob/master/docs/quickstart.md#create-a-postgres-cluster)

## Using Kratix as Operator Composition Pattern

Again, let's abstract away the complexity of Zalando's CRD but this time by using the `kratix` solution. 

Start by installing a single node Kratix instance by following those [instructions](https://docs.kratix.io/main/quick-start), this quickstart will also install a kratix `Promise` that abstracts away Zalando's postgres Operator. 

## Adding an Internal Developer Portal 

Let's add the final piece of our minimal Internal Developer Platform by adding an Internal Developer Portal.

Create a new account on [Port](https://app.port.io/) it's free and no credit card is asked. Be sure to not use a gmail email to be able to create your account. 

During the onboarding of your new account, make sure to choose Gitub and Kubernetes, this will create for you all the blueprints and dashboards that you need. 

Install the Kubernetes data source exporter, basically it's an helm chart to deploy on your cluster. 

Check if the different namespaces, deployments are being synced, take a look at the different software catalogs. 

Now let's configure it to be able to sync the Kratix Promise. In the mapping section of the Kubernetes exporter add this line at the beginning : 

`crdsToDiscover: .spec.group == "marketplace.kratix.io"`

Save & Sync. 

Now fork this [repo](https://github.com/port-labs/control-plane-demo) and make sure to store your `PORT_CLIENT_ID` and `PORT_CLIENT_SECRET` as secrets for your GitHub Actions. 

Make sure to update or create a new ArgoCD application pointing to this newly forked repository. 

Now let's update the Self-Service Action that has been scaffolded for us for the Kratix Postgres Pormise. Go to the `edit` tab and make to configure correctly the `Backend` section. Also make sure that the form displays all the needed fields. 

Now, you should be able to self-serve a Postgres instance using your Internal Developer Portal. 


