/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TextFileContentUpdateComponent from '@/entities/text-file-content/text-file-content-update.vue';
import TextFileContentClass from '@/entities/text-file-content/text-file-content-update.component';
import TextFileContentService from '@/entities/text-file-content/text-file-content.service';

import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('TextFileContent Management Update Component', () => {
    let wrapper: Wrapper<TextFileContentClass>;
    let comp: TextFileContentClass;
    let textFileContentServiceStub: SinonStubbedInstance<TextFileContentService>;

    beforeEach(() => {
      textFileContentServiceStub = sinon.createStubInstance<TextFileContentService>(TextFileContentService);

      wrapper = shallowMount<TextFileContentClass>(TextFileContentUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          textFileContentService: () => textFileContentServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        comp.textFileContent = entity;
        textFileContentServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(textFileContentServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.textFileContent = entity;
        textFileContentServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(textFileContentServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTextFileContent = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        textFileContentServiceStub.find.resolves(foundTextFileContent);
        textFileContentServiceStub.retrieve.resolves([foundTextFileContent]);

        // WHEN
        comp.beforeRouteEnter({ params: { textFileContentId: '9fec3727-3421-4967-b213-ba36557ca194' } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.textFileContent).toBe(foundTextFileContent);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
