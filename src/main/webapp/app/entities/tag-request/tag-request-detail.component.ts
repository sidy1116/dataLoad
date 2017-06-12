import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , DataUtils } from 'ng-jhipster';

import { TagRequest } from './tag-request.model';
import { TagRequestService } from './tag-request.service';

@Component({
    selector: 'jhi-tag-request-detail',
    templateUrl: './tag-request-detail.component.html'
})
export class TagRequestDetailComponent implements OnInit, OnDestroy {

    tagRequest: TagRequest;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private dataUtils: DataUtils,
        private tagRequestService: TagRequestService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTagRequests();
    }

    load(id) {
        this.tagRequestService.find(id).subscribe((tagRequest) => {
            this.tagRequest = tagRequest;
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

    registerChangeInTagRequests() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tagRequestListModification',
            (response) => this.load(this.tagRequest.id)
        );
    }
}
