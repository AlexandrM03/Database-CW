import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTable } from '@angular/material/table';
import { ActivatedRoute } from '@angular/router';
import { DriverRating } from 'src/app/models/DriverRating';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-admin-rating',
  templateUrl: './admin-rating.component.html',
  styleUrls: ['./admin-rating.component.css']
})
export class AdminRatingComponent implements OnInit {
  username: string;
  ratings: DriverRating[];
  isRatingLoaded: boolean = false;
  @ViewChild(MatTable) table: MatTable<DriverRating>;

  constructor(private adminSerive: AdminService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.username = params['username'];
      this.adminSerive.getDriverRating(this.username).subscribe(data => {
        this.ratings = data;
        this.isRatingLoaded = true;
      });
    });
  }

  deleteDriverRating(id: number) {
    this.adminSerive.removeDriverRating(id).subscribe(() => {
      this.ratings = this.ratings.filter(rating => rating.id !== id);
      this.table.renderRows();
    });
  }
}
