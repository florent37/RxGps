package com.github.florent37.rxgps.sample;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.rxgps.RxGps;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

	private RxGps rxGps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rxGps = new RxGps(this, AndroidSchedulers.mainThread(), Schedulers.io());

	}

	@Override
	protected void onStart() {
		super.onStart();
		locationHighAccuracy();
		locationLowPower();
		locationBalancedPowerAccuracy();
		locationNoPower();
		lastLocation();
		geoCode();
	}

	private void locationHighAccuracy() {
		addDisposable(rxGps.locationHighAccuracy()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
				location -> displayLocation(findViewById(R.id.tv_location_high_accuracy_location), location),
				this::displayError));
	}

	private void locationLowPower() {
		addDisposable(rxGps.locationLowPower()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
				location -> displayLocation(findViewById(R.id.tv_location_low_power), location),
				this::displayError));
	}

	private void locationBalancedPowerAccuracy() {
		addDisposable(rxGps.locationBalancedPowerAccuracy()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
				location -> displayLocation(findViewById(R.id.tv_location_balanced_power_accuracy), location),
				this::displayError));
	}

	private void locationNoPower() {
		addDisposable(rxGps.locationNoPower()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
				location -> displayLocation(findViewById(R.id.tv_location_no_power), location),
				this::displayError));
	}

	private void lastLocation() {
		addDisposable(rxGps.lastLocation()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(
				location -> displayLocation(findViewById(R.id.tv_last_location), location),
				this::displayError));
	}

	private void geoCode() {
		addDisposable(rxGps.locationLowPower()
			.flatMapMaybe(rxGps::geoCoding)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(address -> {
				displayAddressText(findViewById(R.id.tv_geo_code), address);
			}, this::displayError));
	}

	private void displayLocation(TextView textView, Location location) {
		textView.setText(location.getLatitude() + ", " + location.getLongitude());
	}

	private void displayError(Throwable throwable) {
		Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
	}

	private void displayAddressText(TextView textView, Address address) {
		StringBuilder addressText = new StringBuilder();
		final int maxAddressLineIndex = address.getMaxAddressLineIndex();

		for (int i = 0; i <= maxAddressLineIndex; i++) {
			addressText.append(address.getAddressLine(i));
			if (i != maxAddressLineIndex) {
				addressText.append("\n");
			}
		}

		textView.setText(addressText.toString());
	}

}
