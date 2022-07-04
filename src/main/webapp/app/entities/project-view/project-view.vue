<template>
  <div>
    <h2 id="page-heading" data-cy="ProjectViewHeading">
      <span v-text="$t('archiscapeCoreApp.projectView.home.title')" id="project-view-heading">Project Views</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('archiscapeCoreApp.projectView.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ProjectViewCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-project-view"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('archiscapeCoreApp.projectView.home.createLabel')"> Create a new Project View </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && projectViews && projectViews.length === 0">
      <span v-text="$t('archiscapeCoreApp.projectView.home.notFound')">No projectViews found</span>
    </div>
    <div class="table-responsive" v-if="projectViews && projectViews.length > 0">
      <table class="table table-striped" aria-describedby="projectViews">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="$t('archiscapeCoreApp.projectView.name')">Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('documentation')">
              <span v-text="$t('archiscapeCoreApp.projectView.documentation')">Documentation</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'documentation'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('c4level')">
              <span v-text="$t('archiscapeCoreApp.projectView.c4level')">C 4 Level</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'c4level'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('extElementId')">
              <span v-text="$t('archiscapeCoreApp.projectView.extElementId')">Ext Element Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'extElementId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('image')">
              <span v-text="$t('archiscapeCoreApp.projectView.image')">Image</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'image'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('project.id')">
              <span v-text="$t('archiscapeCoreApp.projectView.project')">Project</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'project.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('projectElement.id')">
              <span v-text="$t('archiscapeCoreApp.projectView.projectElement')">Project Element</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'projectElement.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="projectView in projectViews" :key="projectView.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ProjectViewView', params: { projectViewId: projectView.id } }">{{ projectView.id }}</router-link>
            </td>
            <td>{{ projectView.name }}</td>
            <td>{{ projectView.documentation }}</td>
            <td v-text="$t('archiscapeCoreApp.C4ViewLevel.' + projectView.c4level)">{{ projectView.c4level }}</td>
            <td>{{ projectView.extElementId }}</td>
            <td>
              <a v-if="projectView.image" v-on:click="openFile(projectView.imageContentType, projectView.image)">
                <img
                  v-bind:src="'data:' + projectView.imageContentType + ';base64,' + projectView.image"
                  style="max-height: 30px"
                  alt="projectView image"
                />
              </a>
              <span v-if="projectView.image">{{ projectView.imageContentType }}, {{ byteSize(projectView.image) }}</span>
            </td>
            <td>
              <div v-if="projectView.project">
                <router-link :to="{ name: 'ProjectView', params: { projectId: projectView.project.id } }">{{
                  projectView.project.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="projectView.projectElement">
                <router-link :to="{ name: 'ProjectElementView', params: { projectElementId: projectView.projectElement.id } }">{{
                  projectView.projectElement.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ProjectViewView', params: { projectViewId: projectView.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ProjectViewEdit', params: { projectViewId: projectView.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(projectView)"
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
          id="archiscapeCoreApp.projectView.delete.question"
          data-cy="projectViewDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-projectView-heading" v-text="$t('archiscapeCoreApp.projectView.delete.question', { id: removeId })">
          Are you sure you want to delete this Project View?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-projectView"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeProjectView()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="projectViews && projectViews.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./project-view.component.ts"></script>
