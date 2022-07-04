<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="archiscapeCoreApp.projectElement.home.createOrEditLabel"
          data-cy="ProjectElementCreateUpdateHeading"
          v-text="$t('archiscapeCoreApp.projectElement.home.createOrEditLabel')"
        >
          Create or edit a ProjectElement
        </h2>
        <div>
          <div class="form-group" v-if="projectElement.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="projectElement.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.projectElement.name')" for="project-element-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="project-element-name"
              data-cy="name"
              :class="{ valid: !$v.projectElement.name.$invalid, invalid: $v.projectElement.name.$invalid }"
              v-model="$v.projectElement.name.$model"
            />
            <div v-if="$v.projectElement.name.$anyDirty && $v.projectElement.name.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.projectElement.name.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('archiscapeCoreApp.projectElement.documentation')"
              for="project-element-documentation"
              >Documentation</label
            >
            <input
              type="text"
              class="form-control"
              name="documentation"
              id="project-element-documentation"
              data-cy="documentation"
              :class="{ valid: !$v.projectElement.documentation.$invalid, invalid: $v.projectElement.documentation.$invalid }"
              v-model="$v.projectElement.documentation.$model"
            />
            <div v-if="$v.projectElement.documentation.$anyDirty && $v.projectElement.documentation.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.projectElement.documentation.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 2048 })"
              >
                This field cannot be longer than 2048 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.projectElement.technology')" for="project-element-technology"
              >Technology</label
            >
            <input
              type="text"
              class="form-control"
              name="technology"
              id="project-element-technology"
              data-cy="technology"
              :class="{ valid: !$v.projectElement.technology.$invalid, invalid: $v.projectElement.technology.$invalid }"
              v-model="$v.projectElement.technology.$model"
            />
            <div v-if="$v.projectElement.technology.$anyDirty && $v.projectElement.technology.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.projectElement.technology.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.projectElement.c4type')" for="project-element-c4type"
              >C 4 Type</label
            >
            <select
              class="form-control"
              name="c4type"
              :class="{ valid: !$v.projectElement.c4type.$invalid, invalid: $v.projectElement.c4type.$invalid }"
              v-model="$v.projectElement.c4type.$model"
              id="project-element-c4type"
              data-cy="c4type"
              required
            >
              <option
                v-for="c4ElementType in c4ElementTypeValues"
                :key="c4ElementType"
                v-bind:value="c4ElementType"
                v-bind:label="$t('archiscapeCoreApp.C4ElementType.' + c4ElementType)"
              >
                {{ c4ElementType }}
              </option>
            </select>
            <div v-if="$v.projectElement.c4type.$anyDirty && $v.projectElement.c4type.$invalid">
              <small class="form-text text-danger" v-if="!$v.projectElement.c4type.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('archiscapeCoreApp.projectElement.landscapeElementId')"
              for="project-element-landscapeElementId"
              >Landscape Element Id</label
            >
            <input
              type="text"
              class="form-control"
              name="landscapeElementId"
              id="project-element-landscapeElementId"
              data-cy="landscapeElementId"
              :class="{ valid: !$v.projectElement.landscapeElementId.$invalid, invalid: $v.projectElement.landscapeElementId.$invalid }"
              v-model="$v.projectElement.landscapeElementId.$model"
            />
            <div v-if="$v.projectElement.landscapeElementId.$anyDirty && $v.projectElement.landscapeElementId.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.projectElement.landscapeElementId.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 30 })"
              >
                This field cannot be longer than 30 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('archiscapeCoreApp.projectElement.extElementId')"
              for="project-element-extElementId"
              >Ext Element Id</label
            >
            <input
              type="text"
              class="form-control"
              name="extElementId"
              id="project-element-extElementId"
              data-cy="extElementId"
              :class="{ valid: !$v.projectElement.extElementId.$invalid, invalid: $v.projectElement.extElementId.$invalid }"
              v-model="$v.projectElement.extElementId.$model"
            />
            <div v-if="$v.projectElement.extElementId.$anyDirty && $v.projectElement.extElementId.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.projectElement.extElementId.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 100 })"
              >
                This field cannot be longer than 100 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('archiscapeCoreApp.projectElement.extSourceElementId')"
              for="project-element-extSourceElementId"
              >Ext Source Element Id</label
            >
            <input
              type="text"
              class="form-control"
              name="extSourceElementId"
              id="project-element-extSourceElementId"
              data-cy="extSourceElementId"
              :class="{ valid: !$v.projectElement.extSourceElementId.$invalid, invalid: $v.projectElement.extSourceElementId.$invalid }"
              v-model="$v.projectElement.extSourceElementId.$model"
            />
            <div v-if="$v.projectElement.extSourceElementId.$anyDirty && $v.projectElement.extSourceElementId.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.projectElement.extSourceElementId.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 100 })"
              >
                This field cannot be longer than 100 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('archiscapeCoreApp.projectElement.extTargetElementId')"
              for="project-element-extTargetElementId"
              >Ext Target Element Id</label
            >
            <input
              type="text"
              class="form-control"
              name="extTargetElementId"
              id="project-element-extTargetElementId"
              data-cy="extTargetElementId"
              :class="{ valid: !$v.projectElement.extTargetElementId.$invalid, invalid: $v.projectElement.extTargetElementId.$invalid }"
              v-model="$v.projectElement.extTargetElementId.$model"
            />
            <div v-if="$v.projectElement.extTargetElementId.$anyDirty && $v.projectElement.extTargetElementId.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.projectElement.extTargetElementId.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 100 })"
              >
                This field cannot be longer than 100 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.projectElement.project')" for="project-element-project"
              >Project</label
            >
            <select
              class="form-control"
              id="project-element-project"
              data-cy="project"
              name="project"
              v-model="projectElement.project"
              required
            >
              <option v-if="!projectElement.project" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  projectElement.project && projectOption.id === projectElement.project.id ? projectElement.project : projectOption
                "
                v-for="projectOption in projects"
                :key="projectOption.id"
              >
                {{ projectOption.id }}
              </option>
            </select>
          </div>
          <div v-if="$v.projectElement.project.$anyDirty && $v.projectElement.project.$invalid">
            <small class="form-text text-danger" v-if="!$v.projectElement.project.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('archiscapeCoreApp.projectElement.elementConfiguration')"
              for="project-element-elementConfiguration"
              >Element Configuration</label
            >
            <select
              class="form-control"
              id="project-element-elementConfiguration"
              data-cy="elementConfiguration"
              name="elementConfiguration"
              v-model="projectElement.elementConfiguration"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  projectElement.elementConfiguration && elementConfigurationOption.id === projectElement.elementConfiguration.id
                    ? projectElement.elementConfiguration
                    : elementConfigurationOption
                "
                v-for="elementConfigurationOption in elementConfigurations"
                :key="elementConfigurationOption.id"
              >
                {{ elementConfigurationOption.id }}
              </option>
            </select>
          </div>
          <div v-if="$v.projectElement.projectViews.$anyDirty && $v.projectElement.projectViews.$invalid">
            <small class="form-text text-danger" v-if="!$v.projectElement.projectViews.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.projectElement.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./project-element-update.component.ts"></script>
