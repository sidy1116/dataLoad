import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DataLoadSharedModule } from '../../shared';
import {
    TagRequestService,
    TagRequestPopupService,
    TagRequestComponent,
    TagRequestDetailComponent,
    TagRequestDialogComponent,
    TagRequestPopupComponent,
    TagRequestDeletePopupComponent,
    TagRequestDeleteDialogComponent,
    tagRequestRoute,
    tagRequestPopupRoute,
} from './';

const ENTITY_STATES = [
    ...tagRequestRoute,
    ...tagRequestPopupRoute,
];

@NgModule({
    imports: [
        DataLoadSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TagRequestComponent,
        TagRequestDetailComponent,
        TagRequestDialogComponent,
        TagRequestDeleteDialogComponent,
        TagRequestPopupComponent,
        TagRequestDeletePopupComponent,
    ],
    entryComponents: [
        TagRequestComponent,
        TagRequestDialogComponent,
        TagRequestPopupComponent,
        TagRequestDeleteDialogComponent,
        TagRequestDeletePopupComponent,
    ],
    providers: [
        TagRequestService,
        TagRequestPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DataLoadTagRequestModule {}
