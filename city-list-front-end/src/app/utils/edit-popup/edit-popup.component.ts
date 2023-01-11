import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-edit-popup',
  templateUrl: './edit-popup.component.html',
  styleUrls: ['./edit-popup.component.css']
})
export class EditPopupComponent implements OnInit {

  public  cityName = null;
  public tempName = '';
  public selectedFile: any;
  public isFileSelected = false;
  constructor(public dialogRef: MatDialogRef<EditPopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.cityName = data.name;
  }

  ngOnInit(): void {
  }

  onClose(): void {
    this.dialogRef.close({name:this.cityName, file: this.selectedFile});
  }

  handleFileInput($event: any) {
    if($event.target.files[0]) {
      const file = $event.target.files[0];
      if (file.type.includes('image/')) {
        this.selectedFile = $event.target.files[0];
        this.isFileSelected = true;
        this.tempName = '';
      } else {
        this.isFileSelected = false;
      }
    } else {
      this.isFileSelected = false;
    }
  }
}
