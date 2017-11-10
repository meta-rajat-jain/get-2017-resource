import { Component, OnInit } from '@angular/core';

import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {
 
   transform(items: any[], searchText): any {
    console.log('term', searchText);
  
    return searchText 
        ? items.filter(item => item.name.toLowerCase().indexOf(searchText) !== -1)
        : items;
}
}