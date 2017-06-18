import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, DataUtils } from 'ng-jhipster';

import { ReTagProfile } from './re-tag-profile.model';
import { ReTagProfilePopupService } from './re-tag-profile-popup.service';
import { ReTagProfileService } from './re-tag-profile.service';

@Component( {
    selector: 'jhi-re-tag-profile-dialog',
    templateUrl: './re-tag-profile-dialog.component.html'
} )
export class ReTagProfileDialogComponent implements OnInit {

    reTagProfile: ReTagProfile;
    authorities: any[];
    isSaving: boolean;
    createDateDp: any;
    phintRows = [];
    headerRows = [];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: DataUtils,
        private alertService: AlertService,
        private reTagProfileService: ReTagProfileService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    addRow( phintRow: string ) {
        this.phintRows.push( phintRow );

    }
    deleteRow( i: number ) {
        this.phintRows.splice( i, 1 );
    }

    addHeaderRow( key1: string, value1: string ) {
        var tempmap = { key: key1, value: value1 };
        this.headerRows.push( tempmap );

    }
    deleteHeaderRow( i: number ) {
        this.headerRows.splice( i, 1 );
    }


    byteSize( field ) {
        return this.dataUtils.byteSize( field );
    }

    openFile( contentType, field ) {
        return this.dataUtils.openFile( contentType, field );
    }

    setFileData( event, reTagProfile, field, isImage ) {
        if ( event && event.target.files && event.target.files[0] ) {
            const file = event.target.files[0];
            if ( isImage && !/^image\//.test( file.type ) ) {
                return;
            }
            this.dataUtils.toBase64( file, ( base64Data ) => {
                reTagProfile[field] = base64Data;
                reTagProfile[`${field}ContentType`] = file.type;
            } );
        }
    }

    clear() {
        this.activeModal.dismiss( 'cancel' );
    }

    save() {
        this.isSaving = true;
        if ( this.reTagProfile.id !== undefined ) {
            this.subscribeToSaveResponse(
                this.reTagProfileService.update( this.reTagProfile ), false );
        } else {
            
            this.reTagProfile.phint='';
            for (var i = 0; i < this.phintRows.length; i++) {
                if(i==0) {
                   
                    this.reTagProfile.phint=this.reTagProfile.phint+this.phintRows[i];
                }else{
                    this.reTagProfile.phint=this.reTagProfile.phint+'||'+this.phintRows[i];
                }
              
            }
            
            for (var header = 0; header < this.headerRows.length; header++) {
                if(header==0) {
                   
                    this.reTagProfile.headers=this.reTagProfile.headers+this.headerRows[header].key+':'+this.headerRows[header].value;
                }else{
                    this.reTagProfile.headers=this.reTagProfile.headers+'||'+this.headerRows[header].key+':'+this.headerRows[header].value;
                }
              
            }
            console.log("reTagProfile ",  this.reTagProfile);
            this.phintRows = []; 
            this.headerRows = [];
            
            
            this.subscribeToSaveResponse(
                this.reTagProfileService.create( this.reTagProfile ), true );
        }
    }

    private subscribeToSaveResponse( result: Observable<ReTagProfile>, isCreated: boolean ) {
        result.subscribe(( res: ReTagProfile ) =>
            this.onSaveSuccess( res, isCreated ), ( res: Response ) => this.onSaveError( res ) );
    }

    private onSaveSuccess( result: ReTagProfile, isCreated: boolean ) {
        this.alertService.success(
            isCreated ? `A new Re Tag Profile is created with identifier ${result.id}`
                : `A Re Tag Profile is updated with identifier ${result.id}`,
            null, null );

        this.eventManager.broadcast( { name: 'reTagProfileListModification', content: 'OK' } );
        this.isSaving = false;
        this.activeModal.dismiss( result );
    }

    private onSaveError( error ) {
        try {
            error.json();
        } catch ( exception ) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError( error );
    }

    private onError( error ) {
        this.alertService.error( error.message, null, null );
    }
}

@Component( {
    selector: 'jhi-re-tag-profile-popup',
    template: ''
} )
export class ReTagProfilePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reTagProfilePopupService: ReTagProfilePopupService
    ) { }

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(( params ) => {
            if ( params['id'] ) {
                this.modalRef = this.reTagProfilePopupService
                    .open( ReTagProfileDialogComponent, params['id'] );
            } else {
                this.modalRef = this.reTagProfilePopupService
                    .open( ReTagProfileDialogComponent );
            }
        } );
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
