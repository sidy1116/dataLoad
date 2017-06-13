import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { DataLoadTagRequestModule } from './tag-request/tag-request.module';
import { DataLoadReTagProfileModule } from './re-tag-profile/re-tag-profile.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        DataLoadTagRequestModule,
        DataLoadReTagProfileModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DataLoadEntityModule {}
