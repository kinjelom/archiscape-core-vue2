<template>
  <div>
    <h2 id="page-heading" data-cy="ElementConfigurationHeading">
      <span v-text="$t('archiscapeCoreApp.elementConfiguration.home.title')" id="element-configuration-heading"
        >Element Configurations</span
      >
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('archiscapeCoreApp.elementConfiguration.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ElementConfigurationCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-element-configuration"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('archiscapeCoreApp.elementConfiguration.home.createLabel')"> Create a new Element Configuration </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && elementConfigurations && elementConfigurations.length === 0">
      <span v-text="$t('archiscapeCoreApp.elementConfiguration.home.notFound')">No elementConfigurations found</span>
    </div>
    <div class="table-responsive" v-if="elementConfigurations && elementConfigurations.length > 0">
      <table class="table table-striped" aria-describedby="elementConfigurations">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="$t('archiscapeCoreApp.elementConfiguration.name')">Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('documentation')">
              <span v-text="$t('archiscapeCoreApp.elementConfiguration.documentation')">Documentation</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'documentation'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('technology')">
              <span v-text="$t('archiscapeCoreApp.elementConfiguration.technology')">Technology</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'technology'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('eolDate')">
              <span v-text="$t('archiscapeCoreApp.elementConfiguration.eolDate')">Eol Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'eolDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('team.id')">
              <span v-text="$t('archiscapeCoreApp.elementConfiguration.team')">Team</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'team.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="elementConfiguration in elementConfigurations" :key="elementConfiguration.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ElementConfigurationView', params: { elementConfigurationId: elementConfiguration.id } }">{{
                elementConfiguration.id
              }}</router-link>
            </td>
            <td>{{ elementConfiguration.name }}</td>
            <td>{{ elementConfiguration.documentation }}</td>
            <td>{{ elementConfiguration.technology }}</td>
            <td>{{ elementConfiguration.eolDate }}</td>
            <td>
              <div v-if="elementConfiguration.team">
                <router-link :to="{ name: 'TeamView', params: { teamId: elementConfiguration.team.id } }">{{
                  elementConfiguration.team.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ElementConfigurationView', params: { elementConfigurationId: elementConfiguration.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ElementConfigurationEdit', params: { elementConfigurationId: elementConfiguration.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(elementConfiguration)"
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
          id="archiscapeCoreApp.elementConfiguration.delete.question"
          data-cy="elementConfigurationDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p
          id="jhi-delete-elementConfiguration-heading"
          v-text="$t('archiscapeCoreApp.elementConfiguration.delete.question', { id: removeId })"
        >
          Are you sure you want to delete this Element Configuration?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-elementConfiguration"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeElementConfiguration()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="elementConfigurations && elementConfigurations.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./element-configuration.component.ts"></script>
