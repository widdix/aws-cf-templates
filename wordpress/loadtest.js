import http from 'k6/http';
import { check } from 'k6';

export let options = {
  stages: [
    { duration: '30s', target: 5 },
    { duration: '30s', target: 10  },
    { duration: '59m', target: 10  },
  ]
};

function get(url) {
  let res = http.get(url);
  check(res, {
    "status was 200": (r) => r.status == 200
  });
}

export default function() {
  // load home
  get(`${__ENV.BASE_URL}/`)
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/style.css?ver=4.7.5`);
  get(`${__ENV.BASE_URL}/wp-includes/js/jquery/jquery.js?ver=1.12.4`);
  get(`${__ENV.BASE_URL}/wp-includes/js/jquery/jquery-migrate.min.js?ver=1.4.1`);
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/assets/images/header.jpg`);
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/assets/js/skip-link-focus-fix.js?ver=1.0`);
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/assets/js/global.js?ver=1.0`);
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/assets/js/jquery.scrollTo.js?ver=2.1.2`);
  get(`${__ENV.BASE_URL}/wp-includes/js/wp-embed.min.js?ver=4.7.5`);

  // load page
  get(`${__ENV.BASE_URL}/?p=1`);
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/style.css?ver=4.7.5`);
  get(`${__ENV.BASE_URL}/wp-includes/js/jquery/jquery.js?ver=1.12.4`);
  get(`${__ENV.BASE_URL}/wp-includes/js/jquery/jquery-migrate.min.js?ver=1.4.1`);
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/assets/images/header.jpg`);
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/assets/js/skip-link-focus-fix.js?ver=1.0`);
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/assets/js/global.js?ver=1.0`);
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/assets/js/jquery.scrollTo.js?ver=2.1.2`);
  get(`${__ENV.BASE_URL}/wp-includes/js/comment-reply.min.js?ver=4.7.5`);
  get(`${__ENV.BASE_URL}/wp-includes/js/wp-embed.min.js?ver=4.7.5`);

  // load archive
  get(`${__ENV.BASE_URL}/?m=201705`)
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/style.css?ver=4.7.5`);
  get(`${__ENV.BASE_URL}/wp-includes/js/jquery/jquery.js?ver=1.12.4`);
  get(`${__ENV.BASE_URL}/wp-includes/js/jquery/jquery-migrate.min.js?ver=1.4.1`);
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/assets/images/header.jpg`);
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/assets/js/skip-link-focus-fix.js?ver=1.0`);
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/assets/js/global.js?ver=1.0`);
  get(`${__ENV.BASE_URL}/wp-content/themes/twentyseventeen/assets/js/jquery.scrollTo.js?ver=2.1.2`);
  get(`${__ENV.BASE_URL}/wp-includes/js/wp-embed.min.js?ver=4.7.5`);
};
