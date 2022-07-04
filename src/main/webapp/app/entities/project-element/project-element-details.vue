<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="projectElement">
        <h2 class="jh-entity-heading" data-cy="projectElementDetailsHeading">
          <span v-text="$t('archiscapeCoreApp.projectElement.detail.title')">ProjectElement</span> {{ projectElement.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectElement.name')">Name</span>
          </dt>
          <dd>
            <span>{{ projectElement.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectElement.documentation')">Documentation</span>
          </dt>
          <dd>
            <span>{{ projectElement.documentation }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectElement.technology')">Technology</span>
          </dt>
          <dd>
            <span>{{ projectElement.technology }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectElement.c4type')">C 4 Type</span>
          </dt>
          <dd>
            <span v-text="$t('archiscapeCoreApp.C4ElementType.' + projectElement.c4type)">{{ projectElement.c4type }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectElement.landscapeElementId')">Landscape Element Id</span>
          </dt>
          <dd>
            <span>{{ projectElement.landscapeElementId }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectElement.extElementId')">Ext Element Id</span>
          </dt>
          <dd>
            <span>{{ projectElement.extElementId }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectElement.extSourceElementId')">Ext Source Element Id</span>
          </dt>
          <dd>
            <span>{{ projectElement.extSourceElementId }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectElement.extTargetElementId')">Ext Target Element Id</span>
          </dt>
          <dd>
            <span>{{ projectElement.extTargetElementId }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectElement.project')">Project</span>
          </dt>
          <dd>
            <div v-if="projectElement.project">
              <router-link :to="{ name: 'ProjectView', params: { projectId: projectElement.project.id } }">{{
                projectElement.project.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectElement.elementConfiguration')">Element Configuration</span>
          </dt>
          <dd>
            <div v-if="projectElement.elementConfiguration">
              <router-link
                :to="{ name: 'ElementConfigurationView', params: { elementConfigurationId: projectElement.elementConfiguration.id } }"
                >{{ projectElement.elementConfiguration.id }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="projectElement.id"
          :to="{ name: 'ProjectElementEdit', params: { projectElementId: projectElement.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./project-element-details.component.ts"></script>
