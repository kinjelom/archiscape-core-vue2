<template>
  <div>
    <b-dropdown-item to="/team">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.team')">Team</span>
    </b-dropdown-item>
    <b-dropdown-item to="/landscape">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.landscape')">Landscape</span>
    </b-dropdown-item>
    <b-dropdown-item to="/element-configuration">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.elementConfiguration')">Element Configuration</span>
    </b-dropdown-item>
    <b-dropdown-item to="/landscape-element">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.landscapeElement')">Landscape Element</span>
    </b-dropdown-item>
    <b-dropdown-item to="/project">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.project')">Project</span>
    </b-dropdown-item>
    <b-dropdown-item to="/project-element">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.projectElement')">Project Element</span>
    </b-dropdown-item>
    <b-dropdown-item to="/project-view">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.projectView')">Project View</span>
    </b-dropdown-item>
    <b-dropdown-item to="/text-file-content">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.textFileContent')">Text File Content</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
