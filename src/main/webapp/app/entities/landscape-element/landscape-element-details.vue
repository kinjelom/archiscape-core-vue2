<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="landscapeElement">
        <h2 class="jh-entity-heading" data-cy="landscapeElementDetailsHeading">
          <span v-text="$t('archiscapeCoreApp.landscapeElement.detail.title')">LandscapeElement</span> {{ landscapeElement.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('archiscapeCoreApp.landscapeElement.name')">Name</span>
          </dt>
          <dd>
            <span>{{ landscapeElement.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.landscapeElement.documentation')">Documentation</span>
          </dt>
          <dd>
            <span>{{ landscapeElement.documentation }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.landscapeElement.technology')">Technology</span>
          </dt>
          <dd>
            <span>{{ landscapeElement.technology }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.landscapeElement.c4type')">C 4 Type</span>
          </dt>
          <dd>
            <span v-text="$t('archiscapeCoreApp.C4ElementType.' + landscapeElement.c4type)">{{ landscapeElement.c4type }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.landscapeElement.landscape')">Landscape</span>
          </dt>
          <dd>
            <div v-if="landscapeElement.landscape">
              <router-link :to="{ name: 'LandscapeView', params: { landscapeId: landscapeElement.landscape.id } }">{{
                landscapeElement.landscape.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.landscapeElement.elementConfiguration')">Element Configuration</span>
          </dt>
          <dd>
            <div v-if="landscapeElement.elementConfiguration">
              <router-link
                :to="{ name: 'ElementConfigurationView', params: { elementConfigurationId: landscapeElement.elementConfiguration.id } }"
                >{{ landscapeElement.elementConfiguration.id }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="landscapeElement.id"
          :to="{ name: 'LandscapeElementEdit', params: { landscapeElementId: landscapeElement.id } }"
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

<script lang="ts" src="./landscape-element-details.component.ts"></script>
