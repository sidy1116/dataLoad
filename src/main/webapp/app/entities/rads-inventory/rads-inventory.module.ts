import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DataLoadSharedModule } from '../../shared';
import {
    RadsInventoryService,
    RadsInventoryPopupService,
    RadsInventoryComponent,
    RadsInventoryDetailComponent,
    RadsInventoryDialogComponent,
    RadsInventoryPopupComponent,
    RadsInventoryDeletePopupComponent,
    RadsInventoryDeleteDialogComponent,
    radsInventoryRoute,
    radsInventoryPopupRoute,
    RadsInventoryResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...radsInventoryRoute,
    ...radsInventoryPopupRoute,
];

@NgModule({
    imports: [
        DataLoadSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RadsInventoryComponent,
        RadsInventoryDetailComponent,
        RadsInventoryDialogComponent,
        RadsInventoryDeleteDialogComponent,
        RadsInventoryPopupComponent,
        RadsInventoryDeletePopupComponent,
    ],
    entryComponents: [
        RadsInventoryComponent,
        RadsInventoryDialogComponent,
        RadsInventoryPopupComponent,
        RadsInventoryDeleteDialogComponent,
        RadsInventoryDeletePopupComponent,
    ],
    providers: [
        RadsInventoryService,
        RadsInventoryPopupService,
        RadsInventoryResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DataLoadRadsInventoryModule {}
