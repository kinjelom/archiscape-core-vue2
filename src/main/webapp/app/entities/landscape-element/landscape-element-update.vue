<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="archiscapeCoreApp.landscapeElement.home.createOrEditLabel"
          data-cy="LandscapeElementCreateUpdateHeading"
          v-text="$t('archiscapeCoreApp.landscapeElement.home.createOrEditLabel')"
        >
          Create or edit a LandscapeElement
        </h2>
        <div>
          <div class="form-group" v-if="landscapeElement.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="landscapeElement.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.landscapeElement.name')" for="landscape-element-name"
              >Name</label
            >
            <input
              type="text"
              class="form-control"
              name="name"
              id="landscape-element-name"
              data-cy="name"
              :class="{ valid: !$v.landscapeElement.name.$invalid, invalid: $v.landscapeElement.name.$invalid }"
              v-model="$v.landscapeElement.name.$model"
              required
            />
            <div v-if="$v.landscapeElement.name.$anyDirty && $v.landscapeElement.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.landscapeElement.name.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.landscapeElement.name.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('archiscapeCoreApp.landscapeElement.documentation')"
              for="landscape-element-documentation"
              >Documentation</label
            >
            <input
              type="text"
              class="form-control"
              name="documentation"
              id="landscape-element-documentation"
              data-cy="documentation"
              :class="{ valid: !$v.landscapeElement.documentation.$invalid, invalid: $v.landscapeElement.documentation.$invalid }"
              v-model="$v.landscapeElement.documentation.$model"
              required
            />
            <div v-if="$v.landscapeElement.documentation.$anyDirty && $v.landscapeElement.documentation.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.landscapeElement.documentation.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.landscapeElement.documentation.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 2048 })"
              >
                This field cannot be longer than 2048 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('archiscapeCoreApp.landscapeElement.technology')"
              for="landscape-element-technology"
              >Technology</label
            >
            <input
              type="text"
              class="form-control"
              name="technology"
              id="landscape-element-technology"
              data-cy="technology"
              :class="{ valid: !$v.landscapeElement.technology.$invalid, invalid: $v.landscapeElement.technology.$invalid }"
              v-model="$v.landscapeElement.technology.$model"
            />
            <div v-if="$v.landscapeElement.technology.$anyDirty && $v.landscapeElement.technology.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.landscapeElement.technology.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.landscapeElement.c4type')" for="landscape-element-c4type"
              >C 4 Type</label
            >
            <select
              class="form-control"
              name="c4type"
              :class="{ valid: !$v.landscapeElement.c4type.$invalid, invalid: $v.landscapeElement.c4type.$invalid }"
              v-model="$v.landscapeElement.c4type.$model"
              id="landscape-element-c4type"
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
            <div v-if="$v.landscapeElement.c4type.$anyDirty && $v.landscapeElement.c4type.$invalid">
              <small class="form-text text-danger" v-if="!$v.landscapeElement.c4type.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('archiscapeCoreApp.landscapeElement.landscape')" for="landscape-element-landscape"
              >Landscape</label
            >
            <select
              class="form-control"
              id="landscape-element-landscape"
              data-cy="landscape"
              name="landscape"
              v-model="landscapeElement.landscape"
              required
            >
              <option v-if="!landscapeElement.landscape" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  landscapeElement.landscape && landscapeOption.id === landscapeElement.landscape.id
                    ? landscapeElement.landscape
                    : landscapeOption
                "
                v-for="landscapeOption in landscapes"
                :key="landscapeOption.id"
              >
                {{ landscapeOption.id }}
              </option>
            </select>
          </div>
          <div v-if="$v.landscapeElement.landscape.$anyDirty && $v.landscapeElement.landscape.$invalid">
            <small class="form-text text-danger" v-if="!$v.landscapeElement.landscape.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('archiscapeCoreApp.landscapeElement.elementConfiguration')"
              for="landscape-element-elementConfiguration"
              >Element Configuration</label
            >
            <select
              class="form-control"
              id="landscape-element-elementConfiguration"
              data-cy="elementConfiguration"
              name="elementConfiguration"
              v-model="landscapeElement.elementConfiguration"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  landscapeElement.elementConfiguration && elementConfigurationOption.id === landscapeElement.elementConfiguration.id
                    ? landscapeElement.elementConfiguration
                    : elementConfigurationOption
                "
                v-for="elementConfigurationOption in elementConfigurations"
                :key="elementConfigurationOption.id"
              >
                {{ elementConfigurationOption.id }}
              </option>
            </select>
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
            :disabled="$v.landscapeElement.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./landscape-element-update.component.ts"></script>
