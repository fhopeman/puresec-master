var puresec = puresec || {};

puresec.detector = function(detector) {
    var $detector = $(detector);
    var detectorId = $detector.data("detector-id");

    var getId = function() {
        return detectorId;
    };

    var remove = function() {
        $.post("/admin/remove/detector", {
            "id": detectorId
        }, function(response) {
            if (response.state == "SUCCESS") {
                $detector.remove();
            }
        });
    };

    return {
        "getId": getId,
        "remove": remove
    };
};
