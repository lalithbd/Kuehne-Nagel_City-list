import {Injectable, Injector} from '@angular/core';
import {HttpService} from "./http.service";
import {HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {filter, map, take} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class CityService extends HttpService {

  constructor(injector: Injector) {
    super(injector)
  }

  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',"Access-Control-Allow-Origin":"*"
    })
  };

  loadCityImages(isSearch: boolean, pageSize: number, pageNumber: number, searchValue: string): Observable<any> {
    const obj = super.connect()
      .pipe(
        filter((data: any) => data.infoCode === 'cities'),
        take(1),
        map(response => response.body));
    const queryParams = {
      ...this.httpOptions,
      params: new HttpParams()
        .set('page-number', pageNumber)
        .set('page-size', pageSize)
        .set('is-search', isSearch)
        .set('search-value', searchValue)
    };
    const url = '/city-images/get';
    super.getReq(url, queryParams).subscribe(response => {
      response.infoCode = 'cities';
      super.emit(response);
    });
    return obj
  }

  loadByteData(id: number): Observable<Blob> {
    const obj = super.connect()
      .pipe(
        filter((data: any) => (data.infoCode === 'byteData' && data.id === id)),
        take(1),
        map(response => response.body));
    const url = '/city-images/get/'+ id;
    const headers = new HttpHeaders({'Content-Type': 'application/json', responseType: 'blob'});

    super.getReq(url, {
      ...headers, responseType: 'blob' as 'json'}).subscribe(response => {
      response.infoCode = 'byteData';
      response.id = id;
      super.emit(response);
    });
    return obj
  }


  public uploadCity(id: number, file: any, name: string) {
    const obj = super.connect()
      .pipe(
        filter((data: any) => data.infoCode === 'updateCity'),
        take(1),
        map(response => response.body));

    const url = '/city-images/update';
    const formData = new FormData();
    formData.append('id', id.toString());
    formData.append('file', file);
    formData.append('city-name', name);
    const uploadHttpOptions = new Headers();
    uploadHttpOptions.append('Content-Type', 'multipart/form-data');

    super.putReq(url, formData, uploadHttpOptions).subscribe(response => {
      response.infoCode = 'updateCity';
      super.emit(response);
    });
    return obj
  }
}
