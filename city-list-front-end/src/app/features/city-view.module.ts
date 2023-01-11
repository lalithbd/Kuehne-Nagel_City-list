import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MainLayoutComponent} from "./main-layout/main-layout.component";
import {CityViewerComponent} from "./city-viewer/city-viewer.component";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatCardModule} from "@angular/material/card";
import {MatPaginatorModule} from "@angular/material/paginator";
import {UtilModule} from "../utils/util.module";
import {MatButtonModule} from "@angular/material/button";



@NgModule({
  declarations: [
    MainLayoutComponent,
    CityViewerComponent],
  imports: [
    CommonModule,
    MatSidenavModule,
    MatToolbarModule,
    MatCardModule,
    MatPaginatorModule,
    UtilModule,
    MatButtonModule
  ]
})
export class CityViewModule { }
