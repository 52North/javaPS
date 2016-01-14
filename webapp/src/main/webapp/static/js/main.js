
jQuery(document).ready(function () {
    // load information
    jQuery.getJSON("info", function (data) {
        window.console&&console.log("Got information: " + JSON.stringify(data));

        jQuery("#stats_records").text(data.records.count);
        jQuery("#service_endpoint").text(data.endpoint);
        jQuery("#service_version").text(data.build.version + 
                "|" + data.version.branch + 
                " @ " + data.build.date);
    });

});