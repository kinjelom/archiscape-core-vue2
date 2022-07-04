<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="archiscapeCoreApp.elementConfiguration.home.createOrEditLabel"
          data-cy="ElementConfigurationCreateUpdateHeading"
          v-text="$t('archiscapeCoreApp.elementConfiguration.home.createOrEditLabel')"
        >
          Create or edit a ElementConfiguration
        </h2>
        <div>
          <div class="form-group" v-if="elementConfiguration.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="elementConfiguration.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.elementConfiguration.name')" for="element-configuration-name"
              >Name</label
            >
            <input
              type="text"
              class="form-control"
              name="name"
              id="element-configuration-name"
              data-cy="name"
              :class="{ valid: !$v.elementConfiguration.name.$invalid, invalid: $v.elementConfiguration.name.$invalid }"
              v-model="$v.elementConfiguration.name.$model"
              required
            />
            <div v-if="$v.elementConfiguration.name.$anyDirty && $v.elementConfiguration.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.elementConfiguration.name.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.elementConfiguration.name.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('archiscapeCoreApp.elementConfiguration.documentation')"
              for="element-configuration-documentation"
              >Documentation</label
            >
            <input
              type="text"
              class="form-control"
              name="documentation"
              id="element-configuration-documentation"
              data-cy="documentation"
              :class="{ valid: !$v.elementConfiguration.documentation.$invalid, invalid: $v.elementConfiguration.documentation.$invalid }"
              v-model="$v.elementConfiguration.documentation.$model"
              required
            />
            <div v-if="$v.elementConfiguration.documentation.$anyDirty && $v.elementConfiguration.documentation.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.elementConfiguration.documentation.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.elementConfiguration.documentation.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 2048 })"
              >
                This field cannot be longer than 2048 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('archiscapeCoreApp.elementConfiguration.technology')"
              for="element-configuration-technology"
              >Technology</label
            >
            <input
              type="text"
              class="form-control"
              name="technology"
              id="element-configuration-technology"
              data-cy="technology"
              :class="{ valid: !$v.elementConfiguration.technology.$invalid, invalid: $v.elementConfiguration.technology.$invalid }"
              v-model="$v.elementConfiguration.technology.$model"
              required
            />
            <div v-if="$v.elementConfiguration.technology.$anyDirty && $v.elementConfiguration.technology.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.elementConfiguration.technology.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.elementConfiguration.technology.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('archiscapeCoreApp.elementConfiguration.eolDate')"
              for="element-configuration-eolDate"
              >Eol Date</label
            >
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="element-configuration-eolDate"
                  v-model="$v.elementConfiguration.eolDate.$model"
                  name="eolDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="element-configuration-eolDate"
                data-cy="eolDate"
                type="text"
                class="form-control"
                name="eolDate"
                :class="{ valid: !$v.elementConfiguration.eolDate.$invalid, invalid: $v.elementConfiguration.eolDate.$invalid }"
                v-model="$v.elementConfiguration.eolDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.elementConfiguration.team')" for="element-configuration-team"
              >Team</label
            >
            <select
              class="form-control"
              id="element-configuration-team"
              data-cy="team"
              name="team"
              v-model="elementConfiguration.team"
              required
            >
              <option v-if="!elementConfiguration.team" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  elementConfiguration.team && teamOption.id === elementConfiguration.team.id ? elementConfiguration.team : teamOption
                "
                v-for="teamOption in teams"
                :key="teamOption.id"
              >
                {{ teamOption.id }}
              </option>
            </select>
          </div>
          <div v-if="$v.elementConfiguration.team.$anyDirty && $v.elementConfiguration.team.$invalid">
            <small class="form-text text-danger" v-if="!$v.elementConfiguration.team.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.elementConfiguration.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./element-configuration-update.component.ts"></script>
