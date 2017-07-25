import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { RadsCategoryInventory } from './rads-category-inventory.model';
import { RadsCategoryInventoryService } from './rads-category-inventory.service';

@Component({
    selector: 'jhi-rads-category-inventory-detail',
    templateUrl: './rads-category-inventory-detail.component.html'
})
export class RadsCategoryInventoryDetailComponent implements OnInit, OnDestroy {

    radsCategoryInventory: RadsCategoryInventory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private radsCategoryInventoryService: RadsCategoryInventoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRadsCategoryInventories();
    }

    load(id) {
        this.radsCategoryInventoryService.find(id).subscribe((radsCategoryInventory) => {
            this.radsCategoryInventory = radsCategoryInventory;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRadsCategoryInventories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'radsCategoryInventoryListModification',
            (response) => this.load(this.radsCategoryInventory.id)
        );
    }
}
