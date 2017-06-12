import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { DataLoadTagRequestModule } from './tag-request/tag-request.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        DataLoadTagRequestModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DataLoadEntityModule {}
