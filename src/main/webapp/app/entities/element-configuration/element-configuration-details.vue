<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="elementConfiguration">
        <h2 class="jh-entity-heading" data-cy="elementConfigurationDetailsHeading">
          <span v-text="$t('archiscapeCoreApp.elementConfiguration.detail.title')">ElementConfiguration</span> {{ elementConfiguration.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('archiscapeCoreApp.elementConfiguration.name')">Name</span>
          </dt>
          <dd>
            <span>{{ elementConfiguration.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.elementConfiguration.documentation')">Documentation</span>
          </dt>
          <dd>
            <span>{{ elementConfiguration.documentation }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.elementConfiguration.technology')">Technology</span>
          </dt>
          <dd>
            <span>{{ elementConfiguration.technology }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.elementConfiguration.eolDate')">Eol Date</span>
          </dt>
          <dd>
            <span>{{ elementConfiguration.eolDate }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.elementConfiguration.team')">Team</span>
          </dt>
          <dd>
            <div v-if="elementConfiguration.team">
              <router-link :to="{ name: 'TeamView', params: { teamId: elementConfiguration.team.id } }">{{
                elementConfiguration.team.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="elementConfiguration.id"
          :to="{ name: 'ElementConfigurationEdit', params: { elementConfigurationId: elementConfiguration.id } }"
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

<script lang="ts" src="./element-configuration-details.component.ts"></script>
