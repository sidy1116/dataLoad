import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DataLoadSharedModule } from '../../shared';
import {
    RadsCategoryInventoryService,
    RadsCategoryInventoryPopupService,
    RadsCategoryInventoryComponent,
    RadsCategoryInventoryDetailComponent,
    RadsCategoryInventoryDialogComponent,
    RadsCategoryInventoryPopupComponent,
    RadsCategoryInventoryDeletePopupComponent,
    RadsCategoryInventoryDeleteDialogComponent,
    radsCategoryInventoryRoute,
    radsCategoryInventoryPopupRoute,
    RadsCategoryInventoryResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...radsCategoryInventoryRoute,
    ...radsCategoryInventoryPopupRoute,
];

@NgModule({
    imports: [
        DataLoadSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RadsCategoryInventoryComponent,
        RadsCategoryInventoryDetailComponent,
        RadsCategoryInventoryDialogComponent,
        RadsCategoryInventoryDeleteDialogComponent,
        RadsCategoryInventoryPopupComponent,
        RadsCategoryInventoryDeletePopupComponent,
    ],
    entryComponents: [
        RadsCategoryInventoryComponent,
        RadsCategoryInventoryDialogComponent,
        RadsCategoryInventoryPopupComponent,
        RadsCategoryInventoryDeleteDialogComponent,
        RadsCategoryInventoryDeletePopupComponent,
    ],
    providers: [
        RadsCategoryInventoryService,
        RadsCategoryInventoryPopupService,
        RadsCategoryInventoryResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DataLoadRadsCategoryInventoryModule {}
