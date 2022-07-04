<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="archiscapeCoreApp.project.home.createOrEditLabel"
          data-cy="ProjectCreateUpdateHeading"
          v-text="$t('archiscapeCoreApp.project.home.createOrEditLabel')"
        >
          Create or edit a Project
        </h2>
        <div>
          <div class="form-group" v-if="project.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="project.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.project.name')" for="project-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="project-name"
              data-cy="name"
              :class="{ valid: !$v.project.name.$invalid, invalid: $v.project.name.$invalid }"
              v-model="$v.project.name.$model"
              required
            />
            <div v-if="$v.project.name.$anyDirty && $v.project.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.project.name.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.project.name.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.project.description')" for="project-description"
              >Description</label
            >
            <input
              type="text"
              class="form-control"
              name="description"
              id="project-description"
              data-cy="description"
              :class="{ valid: !$v.project.description.$invalid, invalid: $v.project.description.$invalid }"
              v-model="$v.project.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.project.extProjectId')" for="project-extProjectId"
              >Ext Project Id</label
            >
            <input
              type="text"
              class="form-control"
              name="extProjectId"
              id="project-extProjectId"
              data-cy="extProjectId"
              :class="{ valid: !$v.project.extProjectId.$invalid, invalid: $v.project.extProjectId.$invalid }"
              v-model="$v.project.extProjectId.$model"
            />
            <div v-if="$v.project.extProjectId.$anyDirty && $v.project.extProjectId.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.project.extProjectId.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 100 })"
              >
                This field cannot be longer than 100 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.project.active')" for="project-active">Active</label>
            <input
              type="checkbox"
              class="form-check"
              name="active"
              id="project-active"
              data-cy="active"
              :class="{ valid: !$v.project.active.$invalid, invalid: $v.project.active.$invalid }"
              v-model="$v.project.active.$model"
              required
            />
            <div v-if="$v.project.active.$anyDirty && $v.project.active.$invalid">
              <small class="form-text text-danger" v-if="!$v.project.active.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.project.srcUrl')" for="project-srcUrl">Src Url</label>
            <input
              type="text"
              class="form-control"
              name="srcUrl"
              id="project-srcUrl"
              data-cy="srcUrl"
              :class="{ valid: !$v.project.srcUrl.$invalid, invalid: $v.project.srcUrl.$invalid }"
              v-model="$v.project.srcUrl.$model"
            />
            <div v-if="$v.project.srcUrl.$anyDirty && $v.project.srcUrl.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.project.srcUrl.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 2048 })"
              >
                This field cannot be longer than 2048 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.project.contentStoreUrl')" for="project-contentStoreUrl"
              >Content Store Url</label
            >
            <input
              type="text"
              class="form-control"
              name="contentStoreUrl"
              id="project-contentStoreUrl"
              data-cy="contentStoreUrl"
              :class="{ valid: !$v.project.contentStoreUrl.$invalid, invalid: $v.project.contentStoreUrl.$invalid }"
              v-model="$v.project.contentStoreUrl.$model"
            />
            <div v-if="$v.project.contentStoreUrl.$anyDirty && $v.project.contentStoreUrl.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.project.contentStoreUrl.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 2048 })"
              >
                This field cannot be longer than 2048 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.project.landscape')" for="project-landscape">Landscape</label>
            <select class="form-control" id="project-landscape" data-cy="landscape" name="landscape" v-model="project.landscape" required>
              <option v-if="!project.landscape" v-bind:value="null" selected></option>
              <option
                v-bind:value="project.landscape && landscapeOption.id === project.landscape.id ? project.landscape : landscapeOption"
                v-for="landscapeOption in landscapes"
                :key="landscapeOption.id"
              >
                {{ landscapeOption.id }}
              </option>
            </select>
          </div>
          <div v-if="$v.project.landscape.$anyDirty && $v.project.landscape.$invalid">
            <small class="form-text text-danger" v-if="!$v.project.landscape.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.project.team')" for="project-team">Team</label>
            <select class="form-control" id="project-team" data-cy="team" name="team" v-model="project.team" required>
              <option v-if="!project.team" v-bind:value="null" selected></option>
              <option
                v-bind:value="project.team && teamOption.id === project.team.id ? project.team : teamOption"
                v-for="teamOption in teams"
                :key="teamOption.id"
              >
                {{ teamOption.id }}
              </option>
            </select>
          </div>
          <div v-if="$v.project.team.$anyDirty && $v.project.team.$invalid">
            <small class="form-text text-danger" v-if="!$v.project.team.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.project.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./project-update.component.ts"></script>
