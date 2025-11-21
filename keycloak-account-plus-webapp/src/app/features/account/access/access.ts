import {Component} from '@angular/core';
import {
  MatAccordion,
  MatExpansionPanel, MatExpansionPanelDescription,
  MatExpansionPanelHeader,
  MatExpansionPanelTitle
} from '@angular/material/expansion';
import {DatePipe} from '@angular/common';
import {MatList, MatListItem} from '@angular/material/list';

@Component({
  selector: 'app-access',
  imports: [
    MatAccordion,
    MatExpansionPanel,
    MatExpansionPanelHeader,
    MatExpansionPanelTitle,
    MatExpansionPanelDescription,
    DatePipe,
    MatList,
    MatListItem
  ],
  templateUrl: './access.html',
  styleUrl: './access.css',
})
export class Access {

  protected readonly access: {
    id: string;
    session: string,
    ip: string,
    startedAt: Date,
    expiredAt: Date,
    lastSeenAt: Date,
    client: string
  }[] = [
    {
      id: 'c78a4d58-0749-4334-99ed-20aafa4aa4ff',
      session: 'Windows 10 / Chrome/142.0.0',
      client: 'Account Console, Security Admin Console',
      ip: '127.0.0.1',
      startedAt: new Date(),
      expiredAt: new Date(),
      lastSeenAt: new Date(),
    },
    {
      id: '20f93e46-3895-4998-9dfa-bc196194d901',
      session: 'Windows 10 / Chrome/142.0.0',
      client: 'Account Console, Security Admin Console',
      ip: '127.0.0.1',
      startedAt: new Date(),
      expiredAt: new Date(),
      lastSeenAt: new Date(),
    }
  ];


}
