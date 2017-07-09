import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DataLoadSharedModule } from '../../shared';
import {
    VerifyUserTagService,
    VerifyUserTagPopupService,
    VerifyUserTagComponent,
    VerifyUserTagDetailComponent,
    VerifyUserTagDialogComponent,
    VerifyUserTagPopupComponent,
    VerifyUserTagDeletePopupComponent,
    VerifyUserTagDeleteDialogComponent,
    verifyUserTagRoute,
    verifyUserTagPopupRoute,
    VerifyUserTagResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...verifyUserTagRoute,
    ...verifyUserTagPopupRoute,
];

@NgModule({
    imports: [
        DataLoadSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        VerifyUserTagComponent,
        VerifyUserTagDetailComponent,
        VerifyUserTagDialogComponent,
        VerifyUserTagDeleteDialogComponent,
        VerifyUserTagPopupComponent,
        VerifyUserTagDeletePopupComponent,
    ],
    entryComponents: [
        VerifyUserTagComponent,
        VerifyUserTagDialogComponent,
        VerifyUserTagPopupComponent,
        VerifyUserTagDeleteDialogComponent,
        VerifyUserTagDeletePopupComponent,
    ],
    providers: [
        VerifyUserTagService,
        VerifyUserTagPopupService,
        VerifyUserTagResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DataLoadVerifyUserTagModule {}
