import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DataLoadSharedModule } from '../../shared';
import {
    ReTagProfileService,
    ReTagProfilePopupService,
    ReTagProfileComponent,
    ReTagProfileDetailComponent,
    ReTagProfileDialogComponent,
    ReTagProfilePopupComponent,
    ReTagProfileDeletePopupComponent,
    ReTagProfileDeleteDialogComponent,
    reTagProfileRoute,
    reTagProfilePopupRoute,
} from './';

const ENTITY_STATES = [
    ...reTagProfileRoute,
    ...reTagProfilePopupRoute,
];

@NgModule({
    imports: [
        DataLoadSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ReTagProfileComponent,
        ReTagProfileDetailComponent,
        ReTagProfileDialogComponent,
        ReTagProfileDeleteDialogComponent,
        ReTagProfilePopupComponent,
        ReTagProfileDeletePopupComponent,
    ],
    entryComponents: [
        ReTagProfileComponent,
        ReTagProfileDialogComponent,
        ReTagProfilePopupComponent,
        ReTagProfileDeleteDialogComponent,
        ReTagProfileDeletePopupComponent,
    ],
    providers: [
        ReTagProfileService,
        ReTagProfilePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DataLoadReTagProfileModule {}
