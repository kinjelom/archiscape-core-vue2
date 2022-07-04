<template>
  <div>
    <h2 id="page-heading" data-cy="ProjectElementHeading">
      <span v-text="$t('archiscapeCoreApp.projectElement.home.title')" id="project-element-heading">Project Elements</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('archiscapeCoreApp.projectElement.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ProjectElementCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-project-element"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('archiscapeCoreApp.projectElement.home.createLabel')"> Create a new Project Element </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && projectElements && projectElements.length === 0">
      <span v-text="$t('archiscapeCoreApp.projectElement.home.notFound')">No projectElements found</span>
    </div>
    <div class="table-responsive" v-if="projectElements && projectElements.length > 0">
      <table class="table table-striped" aria-describedby="projectElements">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="$t('archiscapeCoreApp.projectElement.name')">Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('documentation')">
              <span v-text="$t('archiscapeCoreApp.projectElement.documentation')">Documentation</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'documentation'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('technology')">
              <span v-text="$t('archiscapeCoreApp.projectElement.technology')">Technology</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'technology'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('c4type')">
              <span v-text="$t('archiscapeCoreApp.projectElement.c4type')">C 4 Type</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'c4type'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('landscapeElementId')">
              <span v-text="$t('archiscapeCoreApp.projectElement.landscapeElementId')">Landscape Element Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'landscapeElementId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('extElementId')">
              <span v-text="$t('archiscapeCoreApp.projectElement.extElementId')">Ext Element Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'extElementId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('extSourceElementId')">
              <span v-text="$t('archiscapeCoreApp.projectElement.extSourceElementId')">Ext Source Element Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'extSourceElementId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('extTargetElementId')">
              <span v-text="$t('archiscapeCoreApp.projectElement.extTargetElementId')">Ext Target Element Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'extTargetElementId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('project.id')">
              <span v-text="$t('archiscapeCoreApp.projectElement.project')">Project</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'project.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('elementConfiguration.id')">
              <span v-text="$t('archiscapeCoreApp.projectElement.elementConfiguration')">Element Configuration</span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'elementConfiguration.id'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="projectElement in projectElements" :key="projectElement.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ProjectElementView', params: { projectElementId: projectElement.id } }">{{
                projectElement.id
              }}</router-link>
            </td>
            <td>{{ projectElement.name }}</td>
            <td>{{ projectElement.documentation }}</td>
            <td>{{ projectElement.technology }}</td>
            <td v-text="$t('archiscapeCoreApp.C4ElementType.' + projectElement.c4type)">{{ projectElement.c4type }}</td>
            <td>{{ projectElement.landscapeElementId }}</td>
            <td>{{ projectElement.extElementId }}</td>
            <td>{{ projectElement.extSourceElementId }}</td>
            <td>{{ projectElement.extTargetElementId }}</td>
            <td>
              <div v-if="projectElement.project">
                <router-link :to="{ name: 'ProjectView', params: { projectId: projectElement.project.id } }">{{
                  projectElement.project.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="projectElement.elementConfiguration">
                <router-link
                  :to="{ name: 'ElementConfigurationView', params: { elementConfigurationId: projectElement.elementConfiguration.id } }"
                  >{{ projectElement.elementConfiguration.id }}</router-link
                >
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ProjectElementView', params: { projectElementId: projectElement.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ProjectElementEdit', params: { projectElementId: projectElement.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(projectElement)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span
          id="archiscapeCoreApp.projectElement.delete.question"
          data-cy="projectElementDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-projectElement-heading" v-text="$t('archiscapeCoreApp.projectElement.delete.question', { id: removeId })">
          Are you sure you want to delete this Project Element?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-projectElement"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeProjectElement()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="projectElements && projectElements.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./project-element.component.ts"></script>
