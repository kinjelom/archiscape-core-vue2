<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="textFileContent">
        <h2 class="jh-entity-heading" data-cy="textFileContentDetailsHeading">
          <span v-text="$t('archiscapeCoreApp.textFileContent.detail.title')">TextFileContent</span> {{ textFileContent.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('archiscapeCoreApp.textFileContent.version')">Version</span>
          </dt>
          <dd>
            <span>{{ textFileContent.version }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.textFileContent.insertDate')">Insert Date</span>
          </dt>
          <dd>
            <span>{{ textFileContent.insertDate }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.textFileContent.fileName')">File Name</span>
          </dt>
          <dd>
            <span>{{ textFileContent.fileName }}</span>
          </dd>
          <dt>
            <span v-text="$t('archiscapeCoreApp.textFileContent.content')">Content</span>
          </dt>
          <dd>
            <span>{{ textFileContent.content }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="textFileContent.id"
          :to="{ name: 'TextFileContentEdit', params: { textFileContentId: textFileContent.id } }"
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

<script lang="ts" src="./text-file-content-details.component.ts"></script>
