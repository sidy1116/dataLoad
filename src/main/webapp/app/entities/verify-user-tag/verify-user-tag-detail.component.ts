import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , DataUtils } from 'ng-jhipster';

import { VerifyUserTag } from './verify-user-tag.model';
import { VerifyUserTagService } from './verify-user-tag.service';

@Component({
    selector: 'jhi-verify-user-tag-detail',
    templateUrl: './verify-user-tag-detail.component.html'
})
export class VerifyUserTagDetailComponent implements OnInit, OnDestroy {

    verifyUserTag: VerifyUserTag;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private dataUtils: DataUtils,
        private verifyUserTagService: VerifyUserTagService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVerifyUserTags();
    }

    load(id) {
        this.verifyUserTagService.find(id).subscribe((verifyUserTag) => {
            this.verifyUserTag = verifyUserTag;
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

    registerChangeInVerifyUserTags() {
        this.eventSubscriber = this.eventManager.subscribe(
            'verifyUserTagListModification',
            (response) => this.load(this.verifyUserTag.id)
        );
    }
}
