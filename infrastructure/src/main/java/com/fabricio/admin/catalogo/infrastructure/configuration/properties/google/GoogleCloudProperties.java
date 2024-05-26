package com.fabricio.admin.catalogo.infrastructure.configuration.properties.google;

//@Component
//@ConfigurationProperties("google.cloud") //essa é a outra forma de injetar essa classe como bean
public class GoogleCloudProperties {

    private String credentials;

    private String projectId;

    public GoogleCloudProperties() {
    }

    public String getCredentials() {
        return credentials;
    }

    public GoogleCloudProperties setCredentials(String credentials) {
        this.credentials = credentials;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public GoogleCloudProperties setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    @Override
    public String toString() {
        return "GoogleCloudProperties{" +
                "credentials='" + credentials + '\'' + //remover o credentials para não logar
                ", projectId='" + projectId + '\'' +
                '}';
    }
}
