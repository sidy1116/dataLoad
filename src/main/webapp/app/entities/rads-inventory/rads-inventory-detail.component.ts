import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { RadsInventory } from './rads-inventory.model';
import { RadsInventoryService } from './rads-inventory.service';

@Component({
    selector: 'jhi-rads-inventory-detail',
    templateUrl: './rads-inventory-detail.component.html'
})
export class RadsInventoryDetailComponent implements OnInit, OnDestroy {

    radsInventory: RadsInventory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private radsInventoryService: RadsInventoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRadsInventories();
    }

    load(id) {
        this.radsInventoryService.find(id).subscribe((radsInventory) => {
            this.radsInventory = radsInventory;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRadsInventories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'radsInventoryListModification',
            (response) => this.load(this.radsInventory.id)
        );
    }
}
