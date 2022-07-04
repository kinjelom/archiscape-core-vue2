<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="archiscapeCoreApp.textFileContent.home.createOrEditLabel"
          data-cy="TextFileContentCreateUpdateHeading"
          v-text="$t('archiscapeCoreApp.textFileContent.home.createOrEditLabel')"
        >
          Create or edit a TextFileContent
        </h2>
        <div>
          <div class="form-group" v-if="textFileContent.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="textFileContent.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.textFileContent.version')" for="text-file-content-version"
              >Version</label
            >
            <input
              type="number"
              class="form-control"
              name="version"
              id="text-file-content-version"
              data-cy="version"
              :class="{ valid: !$v.textFileContent.version.$invalid, invalid: $v.textFileContent.version.$invalid }"
              v-model.number="$v.textFileContent.version.$model"
              required
            />
            <div v-if="$v.textFileContent.version.$anyDirty && $v.textFileContent.version.$invalid">
              <small class="form-text text-danger" v-if="!$v.textFileContent.version.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.textFileContent.version.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.textFileContent.insertDate')" for="text-file-content-insertDate"
              >Insert Date</label
            >
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="text-file-content-insertDate"
                  v-model="$v.textFileContent.insertDate.$model"
                  name="insertDate"
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
                id="text-file-content-insertDate"
                data-cy="insertDate"
                type="text"
                class="form-control"
                name="insertDate"
                :class="{ valid: !$v.textFileContent.insertDate.$invalid, invalid: $v.textFileContent.insertDate.$invalid }"
                v-model="$v.textFileContent.insertDate.$model"
                required
              />
            </b-input-group>
            <div v-if="$v.textFileContent.insertDate.$anyDirty && $v.textFileContent.insertDate.$invalid">
              <small class="form-text text-danger" v-if="!$v.textFileContent.insertDate.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.textFileContent.fileName')" for="text-file-content-fileName"
              >File Name</label
            >
            <input
              type="text"
              class="form-control"
              name="fileName"
              id="text-file-content-fileName"
              data-cy="fileName"
              :class="{ valid: !$v.textFileContent.fileName.$invalid, invalid: $v.textFileContent.fileName.$invalid }"
              v-model="$v.textFileContent.fileName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.textFileContent.content')" for="text-file-content-content"
              >Content</label
            >
            <textarea
              class="form-control"
              name="content"
              id="text-file-content-content"
              data-cy="content"
              :class="{ valid: !$v.textFileContent.content.$invalid, invalid: $v.textFileContent.content.$invalid }"
              v-model="$v.textFileContent.content.$model"
              required
            ></textarea>
            <div v-if="$v.textFileContent.content.$anyDirty && $v.textFileContent.content.$invalid">
              <small class="form-text text-danger" v-if="!$v.textFileContent.content.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
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
            :disabled="$v.textFileContent.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./text-file-content-update.component.ts"></script>
