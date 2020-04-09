<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

> **New**: [Become a sponsor](https://github.com/sponsors/widdix) via GitHub Sponsors!

# Migrate from v5 to v6

There is one smaller breaking change that affects mostly all templates: The optional parameter `NewRelicLicenseKey` was removed.

[New Relic Servers](https://docs.newrelic.com/docs/servers) and Legacy Alerts are going away on or before May 15, 2018. That's why we removed them from the templates.

If you want to migrate stacks, you can no longer provide the `NewRelicLicenseKey` parameter.
