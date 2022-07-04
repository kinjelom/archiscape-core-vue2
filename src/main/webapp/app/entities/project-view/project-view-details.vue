<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="projectView">
        <h2 class="jh-entity-heading" data-cy="projectViewDetailsHeading">
          <span v-text="$t('archiscapeCoreApp.projectView.detail.title')">ProjectView</span> {{ projectView.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectView.name')">Name</span>
          </dt>
          <dd>
            <span>{{ projectView.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectView.documentation')">Documentation</span>
          </dt>
          <dd>
            <span>{{ projectView.documentation }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectView.c4level')">C 4 Level</span>
          </dt>
          <dd>
            <span v-text="$t('archiscapeCoreApp.C4ViewLevel.' + projectView.c4level)">{{ projectView.c4level }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectView.extElementId')">Ext Element Id</span>
          </dt>
          <dd>
            <span>{{ projectView.extElementId }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectView.image')">Image</span>
          </dt>
          <dd>
            <div v-if="projectView.image">
              <a v-on:click="openFile(projectView.imageContentType, projectView.image)">
                <img
                  v-bind:src="'data:' + projectView.imageContentType + ';base64,' + projectView.image"
                  style="max-width: 100%"
                  alt="projectView image"
                />
              </a>
              {{ projectView.imageContentType }}, {{ byteSize(projectView.image) }}
            </div>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectView.project')">Project</span>
          </dt>
          <dd>
            <div v-if="projectView.project">
              <router-link :to="{ name: 'ProjectView', params: { projectId: projectView.project.id } }">{{
                projectView.project.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.projectView.projectElement')">Project Element</span>
          </dt>
          <dd>
            <div v-if="projectView.projectElement">
              <router-link :to="{ name: 'ProjectElementView', params: { projectElementId: projectView.projectElement.id } }">{{
                projectView.projectElement.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="projectView.id"
          :to="{ name: 'ProjectViewEdit', params: { projectViewId: projectView.id } }"
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

<script lang="ts" src="./project-view-details.component.ts"></script>
