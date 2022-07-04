<template>
  <div>
    <h2 id="page-heading" data-cy="LandscapeElementHeading">
      <span v-text="$t('archiscapeCoreApp.landscapeElement.home.title')" id="landscape-element-heading">Landscape Elements</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('archiscapeCoreApp.landscapeElement.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'LandscapeElementCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-landscape-element"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('archiscapeCoreApp.landscapeElement.home.createLabel')"> Create a new Landscape Element </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && landscapeElements && landscapeElements.length === 0">
      <span v-text="$t('archiscapeCoreApp.landscapeElement.home.notFound')">No landscapeElements found</span>
    </div>
    <div class="table-responsive" v-if="landscapeElements && landscapeElements.length > 0">
      <table class="table table-striped" aria-describedby="landscapeElements">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="$t('archiscapeCoreApp.landscapeElement.name')">Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('documentation')">
              <span v-text="$t('archiscapeCoreApp.landscapeElement.documentation')">Documentation</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'documentation'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('technology')">
              <span v-text="$t('archiscapeCoreApp.landscapeElement.technology')">Technology</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'technology'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('c4type')">
              <span v-text="$t('archiscapeCoreApp.landscapeElement.c4type')">C 4 Type</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'c4type'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('landscape.id')">
              <span v-text="$t('archiscapeCoreApp.landscapeElement.landscape')">Landscape</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'landscape.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('elementConfiguration.id')">
              <span v-text="$t('archiscapeCoreApp.landscapeElement.elementConfiguration')">Element Configuration</span>
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
          <tr v-for="landscapeElement in landscapeElements" :key="landscapeElement.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'LandscapeElementView', params: { landscapeElementId: landscapeElement.id } }">{{
                landscapeElement.id
              }}</router-link>
            </td>
            <td>{{ landscapeElement.name }}</td>
            <td>{{ landscapeElement.documentation }}</td>
            <td>{{ landscapeElement.technology }}</td>
            <td v-text="$t('archiscapeCoreApp.C4ElementType.' + landscapeElement.c4type)">{{ landscapeElement.c4type }}</td>
            <td>
              <div v-if="landscapeElement.landscape">
                <router-link :to="{ name: 'LandscapeView', params: { landscapeId: landscapeElement.landscape.id } }">{{
                  landscapeElement.landscape.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="landscapeElement.elementConfiguration">
                <router-link
                  :to="{ name: 'ElementConfigurationView', params: { elementConfigurationId: landscapeElement.elementConfiguration.id } }"
                  >{{ landscapeElement.elementConfiguration.id }}</router-link
                >
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'LandscapeElementView', params: { landscapeElementId: landscapeElement.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'LandscapeElementEdit', params: { landscapeElementId: landscapeElement.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(landscapeElement)"
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
          id="archiscapeCoreApp.landscapeElement.delete.question"
          data-cy="landscapeElementDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-landscapeElement-heading" v-text="$t('archiscapeCoreApp.landscapeElement.delete.question', { id: removeId })">
          Are you sure you want to delete this Landscape Element?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-landscapeElement"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeLandscapeElement()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="landscapeElements && landscapeElements.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./landscape-element.component.ts"></script>
