import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { DataLoadTagRequestModule } from './tag-request/tag-request.module';
import { DataLoadReTagProfileModule } from './re-tag-profile/re-tag-profile.module';
import { DataLoadVerifyUserTagModule } from './verify-user-tag/verify-user-tag.module';
import { DataLoadRadsCategoryInventoryModule } from './rads-category-inventory/rads-category-inventory.module';
import { DataLoadRadsInventoryModule } from './rads-inventory/rads-inventory.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        DataLoadTagRequestModule,
        DataLoadReTagProfileModule,
        DataLoadVerifyUserTagModule,
        DataLoadRadsCategoryInventoryModule,
        DataLoadRadsInventoryModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DataLoadEntityModule {}
