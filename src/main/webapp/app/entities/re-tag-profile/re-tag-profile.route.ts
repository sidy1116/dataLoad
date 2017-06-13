import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ReTagProfileComponent } from './re-tag-profile.component';
import { ReTagProfileDetailComponent } from './re-tag-profile-detail.component';
import { ReTagProfilePopupComponent } from './re-tag-profile-dialog.component';
import { ReTagProfileDeletePopupComponent } from './re-tag-profile-delete-dialog.component';

import { Principal } from '../../shared';

export const reTagProfileRoute: Routes = [
    {
        path: 're-tag-profile',
        component: ReTagProfileComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ReTagProfiles'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 're-tag-profile/:id',
        component: ReTagProfileDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ReTagProfiles'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reTagProfilePopupRoute: Routes = [
    {
        path: 're-tag-profile-new',
        component: ReTagProfilePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ReTagProfiles'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 're-tag-profile/:id/edit',
        component: ReTagProfilePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ReTagProfiles'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 're-tag-profile/:id/delete',
        component: ReTagProfileDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ReTagProfiles'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
