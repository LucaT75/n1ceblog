import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExpertPlatformService } from '../expert-platform-service.model';

@Component({
  selector: 'jhi-expert-platform-service-detail',
  templateUrl: './expert-platform-service-detail.component.html',
})
export class ExpertPlatformServiceDetailComponent implements OnInit {
  expertPlatformService: IExpertPlatformService | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expertPlatformService }) => {
      this.expertPlatformService = expertPlatformService;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
