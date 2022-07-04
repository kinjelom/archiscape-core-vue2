<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="archiscapeCoreApp.projectView.home.createOrEditLabel"
          data-cy="ProjectViewCreateUpdateHeading"
          v-text="$t('archiscapeCoreApp.projectView.home.createOrEditLabel')"
        >
          Create or edit a ProjectView
        </h2>
        <div>
          <div class="form-group" v-if="projectView.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="projectView.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.projectView.name')" for="project-view-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="project-view-name"
              data-cy="name"
              :class="{ valid: !$v.projectView.name.$invalid, invalid: $v.projectView.name.$invalid }"
              v-model="$v.projectView.name.$model"
            />
            <div v-if="$v.projectView.name.$anyDirty && $v.projectView.name.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.projectView.name.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.projectView.documentation')" for="project-view-documentation"
              >Documentation</label
            >
            <input
              type="text"
              class="form-control"
              name="documentation"
              id="project-view-documentation"
              data-cy="documentation"
              :class="{ valid: !$v.projectView.documentation.$invalid, invalid: $v.projectView.documentation.$invalid }"
              v-model="$v.projectView.documentation.$model"
            />
            <div v-if="$v.projectView.documentation.$anyDirty && $v.projectView.documentation.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.projectView.documentation.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 2048 })"
              >
                This field cannot be longer than 2048 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.projectView.c4level')" for="project-view-c4level"
              >C 4 Level</label
            >
            <select
              class="form-control"
              name="c4level"
              :class="{ valid: !$v.projectView.c4level.$invalid, invalid: $v.projectView.c4level.$invalid }"
              v-model="$v.projectView.c4level.$model"
              id="project-view-c4level"
              data-cy="c4level"
              required
            >
              <option
                v-for="c4ViewLevel in c4ViewLevelValues"
                :key="c4ViewLevel"
                v-bind:value="c4ViewLevel"
                v-bind:label="$t('archiscapeCoreApp.C4ViewLevel.' + c4ViewLevel)"
              >
                {{ c4ViewLevel }}
              </option>
            </select>
            <div v-if="$v.projectView.c4level.$anyDirty && $v.projectView.c4level.$invalid">
              <small class="form-text text-danger" v-if="!$v.projectView.c4level.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.projectView.extElementId')" for="project-view-extElementId"
              >Ext Element Id</label
            >
            <input
              type="text"
              class="form-control"
              name="extElementId"
              id="project-view-extElementId"
              data-cy="extElementId"
              :class="{ valid: !$v.projectView.extElementId.$invalid, invalid: $v.projectView.extElementId.$invalid }"
              v-model="$v.projectView.extElementId.$model"
            />
            <div v-if="$v.projectView.extElementId.$anyDirty && $v.projectView.extElementId.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.projectView.extElementId.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 100 })"
              >
                This field cannot be longer than 100 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.projectView.image')" for="project-view-image">Image</label>
            <div>
              <img
                v-bind:src="'data:' + projectView.imageContentType + ';base64,' + projectView.image"
                style="max-height: 100px"
                v-if="projectView.image"
                alt="projectView image"
              />
              <div v-if="projectView.image" class="form-text text-danger clearfix">
                <span class="pull-left">{{ projectView.imageContentType }}, {{ byteSize(projectView.image) }}</span>
                <button
                  type="button"
                  v-on:click="clearInputImage('image', 'imageContentType', 'file_image')"
                  class="btn btn-secondary btn-xs pull-right"
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                </button>
              </div>
              <input
                type="file"
                ref="file_image"
                id="file_image"
                data-cy="image"
                v-on:change="setFileData($event, projectView, 'image', true)"
                accept="image/*"
                v-text="$t('entity.action.addimage')"
              />
            </div>
            <input
              type="hidden"
              class="form-control"
              name="image"
              id="project-view-image"
              data-cy="image"
              :class="{ valid: !$v.projectView.image.$invalid, invalid: $v.projectView.image.$invalid }"
              v-model="$v.projectView.image.$model"
            />
            <input
              type="hidden"
              class="form-control"
              name="imageContentType"
              id="project-view-imageContentType"
              v-model="projectView.imageContentType"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.projectView.project')" for="project-view-project"
              >Project</label
            >
            <select class="form-control" id="project-view-project" data-cy="project" name="project" v-model="projectView.project" required>
              <option v-if="!projectView.project" v-bind:value="null" selected></option>
              <option
                v-bind:value="projectView.project && projectOption.id === projectView.project.id ? projectView.project : projectOption"
                v-for="projectOption in projects"
                :key="projectOption.id"
              >
                {{ projectOption.id }}
              </option>
            </select>
          </div>
          <div v-if="$v.projectView.project.$anyDirty && $v.projectView.project.$invalid">
            <small class="form-text text-danger" v-if="!$v.projectView.project.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.projectView.projectElement')" for="project-view-projectElement"
              >Project Element</label
            >
            <select
              class="form-control"
              id="project-view-projectElement"
              data-cy="projectElement"
              name="projectElement"
              v-model="projectView.projectElement"
              required
            >
              <option v-if="!projectView.projectElement" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  projectView.projectElement && projectElementOption.id === projectView.projectElement.id
                    ? projectView.projectElement
                    : projectElementOption
                "
                v-for="projectElementOption in projectElements"
                :key="projectElementOption.id"
              >
                {{ projectElementOption.id }}
              </option>
            </select>
          </div>
          <div v-if="$v.projectView.projectElement.$anyDirty && $v.projectView.projectElement.$invalid">
            <small class="form-text text-danger" v-if="!$v.projectView.projectElement.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.projectView.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./project-view-update.component.ts"></script>
