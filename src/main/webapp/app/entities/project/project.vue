<template>
  <div>
    <h2 id="page-heading" data-cy="ProjectHeading">
      <span v-text="$t('archiscapeCoreApp.project.home.title')" id="project-heading">Projects</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('archiscapeCoreApp.project.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ProjectCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-project"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('archiscapeCoreApp.project.home.createLabel')"> Create a new Project </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && projects && projects.length === 0">
      <span v-text="$t('archiscapeCoreApp.project.home.notFound')">No projects found</span>
    </div>
    <div class="table-responsive" v-if="projects && projects.length > 0">
      <table class="table table-striped" aria-describedby="projects">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="$t('archiscapeCoreApp.project.name')">Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('description')">
              <span v-text="$t('archiscapeCoreApp.project.description')">Description</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('extProjectId')">
              <span v-text="$t('archiscapeCoreApp.project.extProjectId')">Ext Project Id</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'extProjectId'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('active')">
              <span v-text="$t('archiscapeCoreApp.project.active')">Active</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'active'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('srcUrl')">
              <span v-text="$t('archiscapeCoreApp.project.srcUrl')">Src Url</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'srcUrl'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('contentStoreUrl')">
              <span v-text="$t('archiscapeCoreApp.project.contentStoreUrl')">Content Store Url</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'contentStoreUrl'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('landscape.id')">
              <span v-text="$t('archiscapeCoreApp.project.landscape')">Landscape</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'landscape.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('team.id')">
              <span v-text="$t('archiscapeCoreApp.project.team')">Team</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'team.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="project in projects" :key="project.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ProjectView', params: { projectId: project.id } }">{{ project.id }}</router-link>
            </td>
            <td>{{ project.name }}</td>
            <td>{{ project.description }}</td>
            <td>{{ project.extProjectId }}</td>
            <td>{{ project.active }}</td>
            <td>{{ project.srcUrl }}</td>
            <td>{{ project.contentStoreUrl }}</td>
            <td>
              <div v-if="project.landscape">
                <router-link :to="{ name: 'LandscapeView', params: { landscapeId: project.landscape.id } }">{{
                  project.landscape.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="project.team">
                <router-link :to="{ name: 'TeamView', params: { teamId: project.team.id } }">{{ project.team.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ProjectView', params: { projectId: project.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ProjectEdit', params: { projectId: project.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(project)"
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
        ><span id="archiscapeCoreApp.project.delete.question" data-cy="projectDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-project-heading" v-text="$t('archiscapeCoreApp.project.delete.question', { id: removeId })">
          Are you sure you want to delete this Project?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-project"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeProject()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="projects && projects.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./project.component.ts"></script>
