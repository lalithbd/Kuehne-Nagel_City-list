import { Component, OnInit } from '@angular/core';
import {CityService} from "../../services/city.service";
import {DomSanitizer} from "@angular/platform-browser";
import {MatDialog} from "@angular/material/dialog";
import {EditPopupComponent} from "../../utils/edit-popup/edit-popup.component";

@Component({
  selector: 'app-city-viewer',
  templateUrl: './city-viewer.component.html',
  styleUrls: ['./city-viewer.component.css']
})
export class CityViewerComponent implements OnInit {

  constructor(private cityService: CityService,
              private sanitizer: DomSanitizer,
              private matDialog: MatDialog) { }
  pageNumber = 0;
  pageSize = 10;
  isLoading = true;
  totalElementCount = null;
  noCities : any;
  public cities: any[] = [];
  ngOnInit() {
    this.loadCities();
  }

  loadCities() {
    this.cities = [];
    this.isLoading = true;
      this.cityService.loadCityImages(false, this.pageSize, this.pageNumber, '')
      .subscribe(response => {
        if(this.totalElementCount === null) {
          this.totalElementCount = response.totalElements;
        }
        this.noCities = response.content.length === 0;
        response.content.forEach((cityData:any) => {
          console.log(cityData.name);
          this.cityService.loadByteData(cityData.id).subscribe(data => {
            this.isLoading = false;
            const file = new Blob([data]);
            const urlCreator = window.URL || window.webkitURL;
            const fileURL = urlCreator.createObjectURL(file);
            this.cities.push({id: cityData.id, cityName: cityData.name, byteData: this.sanitizer.bypassSecurityTrustUrl(fileURL)});
          })
        })
      });
  }

  editCity(id: number, name: string) {
      this.matDialog.open(EditPopupComponent, {
        data: {id: id, name: name}, width: '50%', height: '50%'
      }).afterClosed().subscribe(data => {
          console.log(data);
          this.cityService.uploadCity(id, data.file, data.name)
      })
  }

  changePage(pageIndex: number) {
    this.pageNumber = pageIndex;
    this.loadCities();
  }
}
