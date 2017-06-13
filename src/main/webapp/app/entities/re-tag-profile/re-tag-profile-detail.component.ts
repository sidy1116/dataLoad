import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , DataUtils } from 'ng-jhipster';

import { ReTagProfile } from './re-tag-profile.model';
import { ReTagProfileService } from './re-tag-profile.service';

@Component({
    selector: 'jhi-re-tag-profile-detail',
    templateUrl: './re-tag-profile-detail.component.html'
})
export class ReTagProfileDetailComponent implements OnInit, OnDestroy {

    reTagProfile: ReTagProfile;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private dataUtils: DataUtils,
        private reTagProfileService: ReTagProfileService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReTagProfiles();
    }

    load(id) {
        this.reTagProfileService.find(id).subscribe((reTagProfile) => {
            this.reTagProfile = reTagProfile;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReTagProfiles() {
        this.eventSubscriber = this.eventManager.subscribe(
            'reTagProfileListModification',
            (response) => this.load(this.reTagProfile.id)
        );
    }
}
