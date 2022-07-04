<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="project">
        <h2 class="jh-entity-heading" data-cy="projectDetailsHeading">
          <span v-text="$t('archiscapeCoreApp.project.detail.title')">Project</span> {{ project.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('archiscapeCoreApp.project.name')">Name</span>
          </dt>
          <dd>
            <span>{{ project.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.project.description')">Description</span>
          </dt>
          <dd>
            <span>{{ project.description }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.project.extProjectId')">Ext Project Id</span>
          </dt>
          <dd>
            <span>{{ project.extProjectId }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.project.active')">Active</span>
          </dt>
          <dd>
            <span>{{ project.active }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.project.srcUrl')">Src Url</span>
          </dt>
          <dd>
            <span>{{ project.srcUrl }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.project.contentStoreUrl')">Content Store Url</span>
          </dt>
          <dd>
            <span>{{ project.contentStoreUrl }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.project.landscape')">Landscape</span>
          </dt>
          <dd>
            <div v-if="project.landscape">
              <router-link :to="{ name: 'LandscapeView', params: { landscapeId: project.landscape.id } }">{{
                project.landscape.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.project.team')">Team</span>
          </dt>
          <dd>
            <div v-if="project.team">
              <router-link :to="{ name: 'TeamView', params: { teamId: project.team.id } }">{{ project.team.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="project.id" :to="{ name: 'ProjectEdit', params: { projectId: project.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./project-details.component.ts"></script>
