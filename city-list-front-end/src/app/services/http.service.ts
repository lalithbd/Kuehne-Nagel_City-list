import {EventEmitter, Injector} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {Observable, throwError} from "rxjs";
import {environment} from "../../environments/environment";

export abstract class HttpService {

  private emitter: EventEmitter<any>;
  protected http: HttpClient;
  private baseUrl: string;

  protected constructor(injector: Injector) {
    this.baseUrl = environment.baseUrl;
    this.http = injector.get(HttpClient);
    this.emitter = new EventEmitter<any>();
  }

  public getReq(url: string, option?: any): Observable<any> {

    return this.requestCall('GET', url, option);
  }

  public putReq(url: string, body: any, option?: any): Observable<any> {

    return this.requestCall('PUT', url, option, body);
  }

  public connect(): EventEmitter<any> {
    return this.emitter;
  }


  public emit(value: any) {
    this.emitter.emit(value);
  }

  private requestCall(methodType: string, url: string, option: any, body?: any): Observable<any> {
    this.calibrateReqHeaders(option, body);
    const makeObs = (fullUrl: string) => {
      return this.http.request(methodType, fullUrl, option).pipe(
        catchError(err => this.handleError(err)),
        map((res: any) => {
          return res;
        }),
      );
    };
    return makeObs(this.baseUrl + url);
  }

  private calibrateReqHeaders(option: any | null, body?: any) {
    if (option == null) {
      option = {};
    }
    if (!option.responseType) {
      option.responseType = 'json';
    }
    option.observe = 'response';

    if (!!body) {
      option.body = body;
    }

    if (option.headers == null) {
      option.headers = new HttpHeaders();
    }

    if (option.headers.get('Content-Type')) {
      option.headers = option.headers.set('Content-Type', 'application/json');
    }
    return option;
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    return throwError(
      'Something bad happened; please try again later.');
  }
}
