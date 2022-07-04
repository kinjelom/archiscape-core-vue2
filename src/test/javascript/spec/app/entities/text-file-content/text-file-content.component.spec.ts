/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TextFileContentComponent from '@/entities/text-file-content/text-file-content.vue';
import TextFileContentClass from '@/entities/text-file-content/text-file-content.component';
import TextFileContentService from '@/entities/text-file-content/text-file-content.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.component('jhi-sort-indicator', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('TextFileContent Management Component', () => {
    let wrapper: Wrapper<TextFileContentClass>;
    let comp: TextFileContentClass;
    let textFileContentServiceStub: SinonStubbedInstance<TextFileContentService>;

    beforeEach(() => {
      textFileContentServiceStub = sinon.createStubInstance<TextFileContentService>(TextFileContentService);
      textFileContentServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<TextFileContentClass>(TextFileContentComponent, {
        store,
        i18n,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          textFileContentService: () => textFileContentServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      textFileContentServiceStub.retrieve.resolves({ headers: {}, data: [{ id: '9fec3727-3421-4967-b213-ba36557ca194' }] });

      // WHEN
      comp.retrieveAllTextFileContents();
      await comp.$nextTick();

      // THEN
      expect(textFileContentServiceStub.retrieve.called).toBeTruthy();
      expect(comp.textFileContents[0]).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });

    it('should load a page', async () => {
      // GIVEN
      textFileContentServiceStub.retrieve.resolves({ headers: {}, data: [{ id: '9fec3727-3421-4967-b213-ba36557ca194' }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(textFileContentServiceStub.retrieve.called).toBeTruthy();
      expect(comp.textFileContents[0]).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      textFileContentServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(textFileContentServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      textFileContentServiceStub.retrieve.reset();
      textFileContentServiceStub.retrieve.resolves({ headers: {}, data: [{ id: '9fec3727-3421-4967-b213-ba36557ca194' }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(textFileContentServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.textFileContents[0]).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      textFileContentServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: '9fec3727-3421-4967-b213-ba36557ca194' });
      expect(textFileContentServiceStub.retrieve.callCount).toEqual(1);

      comp.removeTextFileContent();
      await comp.$nextTick();

      // THEN
      expect(textFileContentServiceStub.delete.called).toBeTruthy();
      expect(textFileContentServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
