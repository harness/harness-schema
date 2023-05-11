# Copyright 2021 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

workspace(name = "harness_monorepo")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")


http_archive(
    name = "com_github_bazelbuild_buildtools",
    sha256 = "932160d5694e688cb7a05ac38efba4b9a90470c75f39716d85fb1d2f95eec96d",
    strip_prefix = "buildtools-4.0.1",
    url = "https://harness-artifactory.harness.io/artifactory/bazel-buildtools-github/archive/refs/tags/4.0.1.zip",
)

# Workaround for https://github.com/bazelbuild/bazel-gazelle/issues/1285. Ideally,
# we can remove this if gazelle ships a fix since we didn't need it before.
http_archive(
    name = "bazel_skylib",
    sha256 = "f7be3474d42aae265405a592bb7da8e171919d74c16f082a5457840f06054728",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.2.1/bazel-skylib-1.2.1.tar.gz",
        "https://github.com/bazelbuild/bazel-skylib/releases/download/1.2.1/bazel-skylib-1.2.1.tar.gz",
    ],
)

load("@bazel_skylib//:workspace.bzl", "bazel_skylib_workspace")

bazel_skylib_workspace()

http_archive(
    name = "com_google_protobuf",
    sha256 = "6aff9834fd7c540875e1836967c8d14c6897e3785a2efac629f69860fb7834ff",
    strip_prefix = "protobuf-3.15.0",
    urls = [
        "https://harness-artifactory.harness.io/artifactory/protobuf-github/archive/v3.15.0.tar.gz",
        #"https://mirror.bazel.build/github.com/protocolbuffers/protobuf/archive/v3.14.0.tar.gz",
        #"https://github.com/protocolbuffers/protobuf/archive/v3.14.0.tar.gz",
    ],
)

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

#openapi_repositories(
#    codegen_cli_provider = "harness-swagger-v3",
#    codegen_cli_sha256 = "8153719ed24ff1bdcf6d5bbe8713d26091ff85a3b253c2224f6e5c8490e9643d",
#    codegen_cli_version = "3.0.35-harness-SNAPSHOT",
#)

# Download the Go rules
http_archive(
    name = "io_bazel_rules_go",
    sha256 = "56d8c5a5c91e1af73eca71a6fab2ced959b67c86d12ba37feedb0a2dfea441a6",
    urls = [
        "https://harness-artifactory.harness.io/artifactory/rules-go-github/download/v0.37.0/rules_go-v0.37.0.zip",
        #"https://mirror.bazel.build/github.com/bazelbuild/rules_go/releases/download/v0.37.0/rules_go-v0.37.0.zip",
        #"https://github.com/bazelbuild/rules_go/releases/download/v0.37.0/rules_go-v0.37.0.zip",
    ],
)

http_archive(
    name = "bazel_gazelle",
    sha256 = "448e37e0dbf61d6fa8f00aaa12d191745e14f07c31cabfa731f0c8e8a4f41b97",
    urls = [
        "https://harness-artifactory.harness.io/artifactory/bazel-gazelle-github/download/v0.28.0/bazel-gazelle-v0.28.0.tar.gz",
        #"https://mirror.bazel.build/github.com/bazelbuild/bazel-gazelle/releases/download/v0.28.0/bazel-gazelle-v0.28.0.tar.gz",
        #"https://github.com/bazelbuild/bazel-gazelle/releases/download/v0.28.0/bazel-gazelle-v0.28.0.tar.gz",
    ],
)

# Load and call the dependencies
load("@io_bazel_rules_go//go:deps.bzl", "go_register_toolchains", "go_rules_dependencies")
load("@bazel_gazelle//:deps.bzl", "gazelle_dependencies", "go_repository")

go_repository(
    name = "com_github_nbio_st",
    importpath = "github.com/nbio/st",
    sum = "h1:W6apQkHrMkS0Muv8G/TipAy/FJl/rCYT0+EuS8+Z0z4=",
    version = "v0.0.0-20140626010706-e9e8d9816f32",
)

go_repository(
    name = "com_github_antihax_optional",
    importpath = "github.com/antihax/optional",
    sum = "h1:xK2lYat7ZLaVVcIuj82J8kIro4V6kDe0AUDFboUCwcg=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_cncf_xds_go",
    importpath = "github.com/cncf/xds/go",
    sum = "h1:zH8ljVhhq7yC0MIeUL/IviMtY8hx2mK8cN9wEYb8ggw=",
    version = "v0.0.0-20211011173535-cb28da3451f1",
)

go_repository(
    name = "com_github_google_martian_v3",
    importpath = "github.com/google/martian/v3",
    sum = "h1:d8MncMlErDFTwQGBK1xhv026j9kqhvw1Qv9IbWT1VLQ=",
    version = "v3.2.1",
)

go_repository(
    name = "com_google_cloud_go_compute",
    importpath = "cloud.google.com/go/compute",
    sum = "h1:SJYBzih8Jj9EUm6IDirxKG0I0AGWduhtb6BmdqWarw4=",
    version = "v1.0.0",
)

go_repository(
    name = "com_google_cloud_go_iam",
    importpath = "cloud.google.com/go/iam",
    sum = "h1:W2vbGCrE3Z7J/x3WXLxxGl9LMSB2uhsAA7Ss/6u/qRY=",
    version = "v0.1.0",
)

go_repository(
    name = "com_google_cloud_go_secretmanager",
    importpath = "cloud.google.com/go/secretmanager",
    sum = "h1:Wbw6lsRrpatsE8GVpuwYqImn+sY5DmRjaEImYPwcSMY=",
    version = "v1.0.0",
)

go_repository(
    name = "io_opentelemetry_go_proto_otlp",
    importpath = "go.opentelemetry.io/proto/otlp",
    sum = "h1:rwOQPCuKAKmwGKq2aVNnYIibI6wnV7EvzgfTCzcdGg8=",
    version = "v0.7.0",
)

go_repository(
    name = "com_github_dgrijalva_jwt_go",
    importpath = "github.com/dgrijalva/jwt-go",
    sum = "h1:7qlOGliEKZXTDg6OTjfoBKDXWrumCAMpl/TFQ4/5kLM=",
    version = "v3.2.0+incompatible",
)

go_repository(
    name = "com_github_dgrijalva_jwt_go_v4",
    importpath = "github.com/dgrijalva/jwt-go/v4",
    sum = "h1:CaO/zOnF8VvUfEbhRatPcwKVWamvbYd8tQGRWacE9kU=",
    version = "v4.0.0-preview1",
)

go_repository(
    name = "org_golang_google_grpc_cmd_protoc_gen_go_grpc",
    importpath = "google.golang.org/grpc/cmd/protoc-gen-go-grpc",
    sum = "h1:M1YKkFIboKNieVO5DLUEVzQfGwJD30Nv2jfUgzb5UcE=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_agiledragon_gomonkey_v2",
    importpath = "github.com/agiledragon/gomonkey/v2",
    sum = "h1:k+UnUY0EMNYUFUAQVETGY9uUTxjMdnUkP0ARyJS1zzs=",
    version = "v2.3.1",
)

go_repository(
    name = "com_github_coreos_go_systemd_v22",
    importpath = "github.com/coreos/go-systemd/v22",
    sum = "h1:rtAn27wIbmOGUs7RIbVgPEjb31ehTVniDwPGXyMxm5U=",
    version = "v22.3.3-0.20220203105225-a9a7ef127534",
)

go_repository(
    name = "com_github_godbus_dbus_v5",
    importpath = "github.com/godbus/dbus/v5",
    sum = "h1:9349emZab16e7zQvpmsbtjc18ykshndd8y2PG3sgJbA=",
    version = "v5.0.4",
)

go_repository(
    name = "com_github_golang_jwt_jwt",
    importpath = "github.com/golang-jwt/jwt",
    sum = "h1:IfV12K8xAKAnZqdXVzCZ+TOjboZ2keLg81eXfW3O+oY=",
    version = "v3.2.2+incompatible",
)

go_repository(
    name = "com_github_josharian_intern",
    importpath = "github.com/josharian/intern",
    sum = "h1:vlS4z54oSdjm0bgjRigI+G1HpF+tI+9rE5LLzOg8HmY=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_kylebanks_depth",
    importpath = "github.com/KyleBanks/depth",
    sum = "h1:5h8fQADFrWtarTdtDudMmGsC7GPbOAu6RVB3ffsVFHc=",
    version = "v1.2.1",
)

go_repository(
    name = "com_github_labstack_echo_v4",
    importpath = "github.com/labstack/echo/v4",
    sum = "h1:wPOF1CE6gvt/kmbMR4dGzWvHMPT+sAEUJOwOTtvITVY=",
    version = "v4.9.0",
)

go_repository(
    name = "com_github_labstack_gommon",
    importpath = "github.com/labstack/gommon",
    sum = "h1:OomWaJXm7xR6L1HmEtGyQf26TEn7V6X88mktX9kee9o=",
    version = "v0.3.1",
)

go_repository(
    name = "com_github_otiai10_copy",
    importpath = "github.com/otiai10/copy",
    sum = "h1:hVoPiN+t+7d2nzzwMiDHPSOogsWAStewq3TwU05+clE=",
    version = "v1.7.0",
)

go_repository(
    name = "com_github_otiai10_curr",
    importpath = "github.com/otiai10/curr",
    sum = "h1:TJIWdbX0B+kpNagQrjgq8bCMrbhiuX73M2XwgtDMoOI=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_otiai10_mint",
    importpath = "github.com/otiai10/mint",
    sum = "h1:7JgpsBaN0uMkyju4tbYHu0mnM55hNKVYLsXmwr15NQI=",
    version = "v1.3.3",
)

go_repository(
    name = "com_github_swaggo_echo_swagger",
    importpath = "github.com/swaggo/echo-swagger",
    sum = "h1:8B+yVqjVm7cMy4QBLRUuRaOzrTVAqZahcrgrOSdpC5I=",
    version = "v1.3.4",
)

go_repository(
    name = "com_github_swaggo_files",
    importpath = "github.com/swaggo/files",
    sum = "h1:kAe4YSu0O0UFn1DowNo2MY5p6xzqtJ/wQ7LZynSvGaY=",
    version = "v0.0.0-20220728132757-551d4a08d97a",
)

go_repository(
    name = "com_github_swaggo_swag",
    importpath = "github.com/swaggo/swag",
    sum = "h1:2rgOaLbonWu1PLP6G+/rYjSvPg0jQE0HtrEKuE380eg=",
    version = "v1.8.6",
)

go_repository(
    name = "com_github_urfave_cli_v2",
    importpath = "github.com/urfave/cli/v2",
    sum = "h1:qph92Y649prgesehzOrQjdWyxFOp/QVM+6imKHad91M=",
    version = "v2.3.0",
)

go_repository(
    name = "com_github_valyala_fasttemplate",
    importpath = "github.com/valyala/fasttemplate",
    sum = "h1:TVEnxayobAdVkhQfrfes2IzOB6o+z4roRkPF52WA1u4=",
    version = "v1.2.1",
)

go_repository(
    name = "com_github_go_redis_redismock_v8",
    importpath = "github.com/go-redis/redismock/v8",
    sum = "h1:rtuijPgGynsRB2Y7KDACm09WvjHWS4RaG44Nm7rcj4Y=",
    version = "v8.0.6",
)

go_repository(
    name = "io_opentelemetry_go_otel_metric",
    importpath = "go.opentelemetry.io/otel/metric",
    sum = "h1:dtZ1Ju44gkJkYvo+3qGqVXmf88tc+a42edOywypengg=",
    version = "v0.19.0",
)

go_repository(
    name = "io_opentelemetry_go_otel_oteltest",
    importpath = "go.opentelemetry.io/otel/oteltest",
    sum = "h1:YVfA0ByROYqTwOxqHVZYZExzEpfZor+MU1rU+ip2v9Q=",
    version = "v0.19.0",
)

go_repository(
    name = "io_opentelemetry_go_otel_trace",
    importpath = "go.opentelemetry.io/otel/trace",
    sum = "h1:1ucYlenXIDA1OlHVLDZKX0ObXV5RLaq06DtUKz5e5zc=",
    version = "v0.19.0",
)

go_repository(
    name = "com_github_appleboy_gofight_v2",
    importpath = "github.com/appleboy/gofight/v2",
    sum = "h1:VOy3jow4vIK8BRQJoC/I9muxyYlJ2yb9ht2hZoS3rf4=",
    version = "v2.1.2",
)

go_repository(
    name = "com_github_go_kit_log",
    importpath = "github.com/go-kit/log",
    sum = "h1:7i2K3eKTos3Vc0enKCfnVcgHh2olr/MyfboYq7cAcFw=",
    version = "v0.2.0",
)

go_repository(
    name = "com_github_golang_jwt_jwt_v4",
    importpath = "github.com/golang-jwt/jwt/v4",
    sum = "h1:rcc4lwaZgFMCZ5jxF9ABolDcIHdBytAFgqFPbSJQAYs=",
    version = "v4.4.2",
)

go_repository(
    name = "com_github_googleapis_go_type_adapters",
    importpath = "github.com/googleapis/go-type-adapters",
    sum = "h1:9XdMn+d/G57qq1s8dNc5IesGCXHf6V2HZ2JwRxfA2tA=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_gorilla_securecookie",
    importpath = "github.com/gorilla/securecookie",
    sum = "h1:miw7JPhV+b/lAHSXz4qd/nN9jRiAFV5FwjeKyCS8BvQ=",
    version = "v1.1.1",
)

go_repository(
    name = "com_github_gorilla_sessions",
    importpath = "github.com/gorilla/sessions",
    sum = "h1:DHd3rPN5lE3Ts3D8rKkQ8x/0kqfeNmBAaiSi+o7FsgI=",
    version = "v1.2.1",
)

go_repository(
    name = "com_github_hdrhistogram_hdrhistogram_go",
    importpath = "github.com/HdrHistogram/hdrhistogram-go",
    sum = "h1:5IcZpTvzydCQeHzK4Ef/D5rrSqwxob0t8PQPMybUNFM=",
    version = "v1.1.2",
)

go_repository(
    name = "com_github_labstack_echo_contrib",
    importpath = "github.com/labstack/echo-contrib",
    sum = "h1:oNUSCeXQOlCGt3eWafzu0mkXjIh3SINnYgE/UR2kYXQ=",
    version = "v0.14.1",
)

go_repository(
    name = "com_github_uber_jaeger_client_go",
    importpath = "github.com/uber/jaeger-client-go",
    sum = "h1:D6wyKGCecFaSRUpo8lCVbaOOb6ThwMmTEbhRwtKR97o=",
    version = "v2.30.0+incompatible",
)

go_repository(
    name = "com_github_uber_jaeger_lib",
    importpath = "github.com/uber/jaeger-lib",
    sum = "h1:td4jdvLcExb4cBISKIpHuGoVXh+dVKhn2Um6rjCsSsg=",
    version = "v2.4.1+incompatible",
)

go_repository(
    name = "com_google_cloud_go_profiler",
    importpath = "cloud.google.com/go/profiler",
    sum = "h1:R6y/xAeifaUXxd2x6w+jIwKxoKl8Cv5HJvcvASTPWJo=",
    version = "v0.3.0",
)

# Add a go repository
go_repository(
    name = "com_github_pkg_errors",
    importpath = "github.com/pkg/errors",
    sum = "h1:FEBLx1zS214owpjy7qsBeixbURkuhQAwrK5UwLGTwt4=",
    version = "v0.9.1",
)

go_repository(
    name = "co_honnef_go_tools",
    importpath = "honnef.co/go/tools",
    sum = "h1:UoveltGrhghAA7ePc+e+QYDHXrBps2PqFZiHkGR/xK8=",
    version = "v0.0.1-2020.1.4",
)

go_repository(
    name = "com_github_bazelbuild_bazel_gazelle",
    importpath = "github.com/bazelbuild/bazel-gazelle",
    sum = "h1:kRymV9q+24Mbeg25fJehw+gvrtVIlwZZAefOSUq4MzU=",
    version = "v0.20.0",
)

# go_repository(
#     name = "com_github_bazelbuild_buildtools",
#     importpath = "github.com/bazelbuild/buildtools",
#     sum = "h1:OfyUN/Msd8yqJww6deQ9vayJWw+Jrbe6Qp9giv51QQI=",
#     version = "v0.0.0-20190731111112-f720930ceb60",
# )

go_repository(
    name = "com_github_go_git_go_git_v5",
    importpath = "github.com/go-git/go-git/v5",
    sum = "h1:BXyZu9t0VkbiHtqrsvdq39UDhGJTl1h55VW6CSC4aY4=",
    version = "v5.4.2",
)

go_repository(
    name = "com_github_emirpasic_gods",
    importpath = "github.com/emirpasic/gods",
    sum = "h1:QAUIPSaCu4G+POclxeqb3F+WPpdKqFGlw36+yOzGlrg=",
    version = "v1.12.0",
)

go_repository(
    name = "com_github_go_git_go_billy_v5",
    importpath = "github.com/go-git/go-billy/v5",
    sum = "h1:CPiOUAzKtMRvolEKw+bG1PLRpT7D3LIs3/3ey4Aiu34=",
    version = "v5.3.1",
)

go_repository(
    name = "com_github_protonmail_go_crypto",
    importpath = "github.com/ProtonMail/go-crypto",
    sum = "h1:YoJbenK9C67SkzkDfmQuVln04ygHj3vjZfd9FL+GmQQ=",
    version = "v0.0.0-20210428141323-04723f9f07d7",
)

go_repository(
    name = "com_github_jbenet_go_context",
    importpath = "github.com/jbenet/go-context",
    sum = "h1:BQSFePA1RWJOlocH6Fxy8MmwDt+yVQYULKfN0RoTN8A=",
    version = "v0.0.0-20150711004518-d14ea06fba99",
)

go_repository(
    name = "com_github_go_git_gcfg",
    importpath = "github.com/go-git/gcfg",
    sum = "h1:Q5ViNfGF8zFgyJWPqYwA7qGFoMTEiBmdlkcfRmpIMa4=",
    version = "v1.5.0",
)

go_repository(
    name = "com_github_kevinburke_ssh_config",
    importpath = "github.com/kevinburke/ssh_config",
    sum = "h1:DowS9hvgyYSX4TO5NpyC606/Z4SxnNYbT+WX27or6Ck=",
    version = "v0.0.0-20201106050909-4977a11b4351",
)

go_repository(
    name = "com_github_xanzy_ssh_agent",
    importpath = "github.com/xanzy/ssh-agent",
    sum = "h1:wUMzuKtKilRgBAD1sUb8gOwwRr2FGoBVumcjoOACClI=",
    version = "v0.3.0",
)

go_repository(
    name = "com_github_bazelbuild_rules_go",
    importpath = "github.com/bazelbuild/rules_go",
    sum = "h1:wzbawlkLtl2ze9w/312NHZ84c7kpUCtlkD8HgFY27sw=",
    version = "v0.0.0-20190719190356-6dae44dc5cab",
)

go_repository(
    name = "com_github_burntsushi_toml",
    importpath = "github.com/BurntSushi/toml",
    sum = "h1:WXkYYl6Yr3qBf1K79EBnL4mak0OimBfB0XUf9Vl28OQ=",
    version = "v0.3.1",
)

go_repository(
    name = "com_github_davecgh_go_spew",
    importpath = "github.com/davecgh/go-spew",
    sum = "h1:vj9j/u1bqnvCEfJOwUhtlOARqs3+rkHYY13jYWTU97c=",
    version = "v1.1.1",
)

go_repository(
    name = "com_github_fsnotify_fsnotify",
    importpath = "github.com/fsnotify/fsnotify",
    sum = "h1:hsms1Qyu0jgnwNXIxa+/V/PDsU6CfLf6CNO8H7IWoS4=",
    version = "v1.4.9",
)

go_repository(
    name = "com_github_google_renameio",
    importpath = "github.com/google/renameio",
    sum = "h1:GOZbcHa3HfsPKPlmyPyN2KEohoMXOhdMbHrvbpl2QaA=",
    version = "v0.1.0",
)

go_repository(
    name = "com_github_kisielk_gotool",
    importpath = "github.com/kisielk/gotool",
    sum = "h1:AV2c/EiW3KqPNT9ZKl07ehoAGi4C5/01Cfbblndcapg=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_kr_pretty",
    importpath = "github.com/kr/pretty",
    sum = "h1:L/CwN0zerZDmRFUapSPitk6f+Q3+0za1rQkzVuMiMFI=",
    version = "v0.1.0",
)

go_repository(
    name = "com_github_kr_pty",
    importpath = "github.com/kr/pty",
    sum = "h1:VkoXIwSboBpnk99O/KFauAEILuNHv5DVFKZMBN/gUgw=",
    version = "v1.1.1",
)

go_repository(
    name = "com_github_kr_text",
    importpath = "github.com/kr/text",
    sum = "h1:5Nx0Ya0ZqY2ygV366QzturHI13Jq95ApcVaJBhpS+AY=",
    version = "v0.2.0",
)

go_repository(
    name = "com_github_mattn_go_shellwords",
    importpath = "github.com/mattn/go-shellwords",
    sum = "h1:Y7Xqm8piKOO3v10Thp7Z36h4FYFjt5xB//6XvOrs2Gw=",
    version = "v1.0.10",
)

go_repository(
    name = "com_github_pelletier_go_toml",
    importpath = "github.com/pelletier/go-toml",
    sum = "h1:T5zMGML61Wp+FlcbWjRDT7yAxhJNAiPPLOFECq181zc=",
    version = "v1.2.0",
)

go_repository(
    name = "com_github_pmezard_go_difflib",
    importpath = "github.com/pmezard/go-difflib",
    sum = "h1:4DBwDE0NGyQoBHbLQYPwSUPoCMWR5BEzIk/f1lZbAQM=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_rogpeppe_go_internal",
    importpath = "github.com/rogpeppe/go-internal",
    sum = "h1:RR9dF3JtopPvtkroDZuVD7qquD0bnHlKSqaQhgwt8yk=",
    version = "v1.3.0",
)

go_repository(
    name = "com_github_stretchr_objx",
    importpath = "github.com/stretchr/objx",
    sum = "h1:4G4v2dO3VZwixGIRoQ5Lfboy6nUhCyYzaqnIAPPhYs4=",
    version = "v0.1.0",
)

go_repository(
    name = "com_github_stretchr_testify",
    importpath = "github.com/stretchr/testify",
    sum = "h1:nwc3DEeHmmLAfoZucVR881uASk0Mfjw8xYJ99tb5CcY=",
    version = "v1.7.0",
)

go_repository(
    name = "in_gopkg_check_v1",
    importpath = "gopkg.in/check.v1",
    sum = "h1:BLraFXnmrev5lT+xlilqcH8XK9/i0At2xKjWk4p6zsU=",
    version = "v1.0.0-20200227125254-8fa46927fb4f",
)

go_repository(
    name = "in_gopkg_errgo_v2",
    importpath = "gopkg.in/errgo.v2",
    sum = "h1:0vLT13EuvQ0hNvakwLuFZ/jYrLp5F3kcWHXdRggjCE8=",
    version = "v2.1.0",
)

go_repository(
    name = "in_gopkg_yaml_v2",
    importpath = "gopkg.in/yaml.v2",
    sum = "h1:D8xgwECY7CYvx+Y2n4sBz93Jn9JRvxdiyyo8CTfuKaY=",
    version = "v2.4.0",
)

go_repository(
    name = "org_golang_x_crypto",
    importpath = "golang.org/x/crypto",
    sum = "h1:kUhD7nTDoI3fVd9G4ORWrbV5NY0liEs/Jg2pv5f+bBA=",
    version = "v0.0.0-20220411220226-7b82a4e95df4",
)

go_repository(
    name = "org_golang_x_lint",
    importpath = "golang.org/x/lint",
    sum = "h1:VLliZ0d+/avPrXXH+OakdXhpJuEoBZuwh1m2j7U6Iug=",
    version = "v0.0.0-20210508222113-6edffad5e616",
)

go_repository(
    name = "org_golang_x_mod",
    importpath = "golang.org/x/mod",
    sum = "h1:kQgndtyPBW/JIYERgdxfwMYh3AVStj88WQTlNDi2a+o=",
    version = "v0.6.0-dev.0.20220106191415-9b9b3d81d5e3",
)

go_repository(
    name = "org_golang_x_sys",
    importpath = "golang.org/x/sys",
    sum = "h1:xHms4gcpe1YE7A3yIllJXP16CMAGuqwO2lX1mTyyRRc=",
    version = "v0.0.0-20220422013727-9388b58f7150",
)

go_repository(
    name = "org_golang_x_text",
    importpath = "golang.org/x/text",
    sum = "h1:olpwvP2KacW1ZWvsR7uQhoyTYvKAupfQrRGBFM352Gk=",
    version = "v0.3.7",
)

go_repository(
    name = "org_golang_x_tools",
    importpath = "golang.org/x/tools",
    sum = "h1:QjFRCZxdOhBJ/UNgnBZLbNV13DlbnK0quyivTnXJM20=",
    version = "v0.1.10",
)

go_repository(
    name = "org_golang_x_xerrors",
    importpath = "golang.org/x/xerrors",
    sum = "h1:go1bK/D/BFZV2I8cIQd1NKEZ+0owSTG1fDTci4IqFcE=",
    version = "v0.0.0-20200804184101-5ec99f83aff1",
)

go_repository(
    name = "org_uber_go_atomic",
    importpath = "go.uber.org/atomic",
    sum = "h1:Ezj3JGmsOnG1MoRWQkPBsKLe9DwWD9QeXzTRzzldNVk=",
    version = "v1.6.0",
)

go_repository(
    name = "org_uber_go_multierr",
    importpath = "go.uber.org/multierr",
    sum = "h1:KCa4XfM8CWFCpxXRGok+Q0SS/0XBhMDbHHGABQLvD2A=",
    version = "v1.5.0",
)

go_repository(
    name = "org_uber_go_tools",
    importpath = "go.uber.org/tools",
    sum = "h1:0mgffUl7nfd+FpvXMVz4IDEaUSmT1ysygQC7qYo7sG4=",
    version = "v0.0.0-20190618225709-2cfd321de3ee",
)

go_repository(
    name = "org_uber_go_zap",
    importpath = "go.uber.org/zap",
    sum = "h1:ZZCA22JRF2gQE5FoNmhmrf7jeJJ2uhqDUNRYKm8dvmM=",
    version = "v1.15.0",
)

go_repository(
    name = "com_github_go_sql_driver_mysql",
    importpath = "github.com/go-sql-driver/mysql",
    sum = "h1:ozyZYNQW3x3HtqT1jira07DN2PArx2v7/mN66gGcHOs=",
    version = "v1.5.0",
)

go_repository(
    name = "com_github_opentracing_opentracing_go",
    importpath = "github.com/opentracing/opentracing-go",
    sum = "h1:uEJPy/1a5RIPAJ0Ov+OIO8OxWu77jEv+1B0VhjKrZUs=",
    version = "v1.2.0",
)

go_repository(
    name = "com_github_satori_go_uuid",
    importpath = "github.com/satori/go.uuid",
    sum = "h1:0uYX9dsZ2yD7q2RtLRtPSdGDWzjeM3TbMJP9utgA0ww=",
    version = "v1.2.0",
)

go_repository(
    name = "in_gopkg_data_dog_go_sqlmock_v1",
    importpath = "gopkg.in/DATA-DOG/go-sqlmock.v1",
    sum = "h1:FVCohIoYO7IJoDDVpV2pdq7SgrMH6wHnuTyrdrxJNoY=",
    version = "v1.3.0",
)

go_repository(
    name = "com_github_alexflint_go_arg",
    importpath = "github.com/alexflint/go-arg",
    sum = "h1:UfldqSdFWeLtoOuVRosqofU4nmhI1pYEbT4ZFS34Bdo=",
    version = "v1.3.0",
)

go_repository(
    name = "com_github_alexflint_go_scalar",
    importpath = "github.com/alexflint/go-scalar",
    sum = "h1:NGupf1XV/Xb04wXskDFzS0KWOLH632W/EO4fAFi+A70=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_golang_mock",
    importpath = "github.com/golang/mock",
    sum = "h1:ErTB+efbowRARo13NNdxyJji2egdxLGQhRaY+DUumQc=",
    version = "v1.6.0",
)

go_repository(
    name = "com_github_aws_aws_sdk_go",
    importpath = "github.com/aws/aws-sdk-go",
    sum = "h1:4Yw8eC4nCXiIVmHJO5PD4oh0vI/df5o6cYTVzFV7vWA=",
    version = "v1.34.29",
)

go_repository(
    name = "com_github_burntsushi_xgb",
    importpath = "github.com/BurntSushi/xgb",
    sum = "h1:1BDTz0u9nC3//pOCMdNH+CiXJVYJh5UQNCOBG7jbELc=",
    version = "v0.0.0-20160522181843-27f122750802",
)

go_repository(
    name = "com_github_census_instrumentation_opencensus_proto",
    importpath = "github.com/census-instrumentation/opencensus-proto",
    sum = "h1:glEXhBS5PSLLv4IXzLA5yPRVX4bilULVyxxbrfOtDAk=",
    version = "v0.2.1",
)

go_repository(
    name = "com_github_chzyer_logex",
    importpath = "github.com/chzyer/logex",
    sum = "h1:Swpa1K6QvQznwJRcfTfQJmTE72DqScAa40E+fbHEXEE=",
    version = "v1.1.10",
)

go_repository(
    name = "com_github_chzyer_readline",
    importpath = "github.com/chzyer/readline",
    sum = "h1:fY5BOSpyZCqRo5OhCuC+XN+r/bBCmeuuJtjz+bCNIf8=",
    version = "v0.0.0-20180603132655-2972be24d48e",
)

go_repository(
    name = "com_github_wings_software_dlite",
    importpath = "github.com/wings-software/dlite",
    sum = "h1:143lMB7T+9OqU9VYSpm71B4DzIONQOUvWWGsmreTpHI=",
    version = "v0.0.0-20220915141325-eb8c295a2189",
)

go_repository(
    name = "com_github_patrickmn_go_cache",
    importpath = "github.com/patrickmn/go-cache",
    sum = "h1:HRMgzkcYKYpi3C8ajMPV8OFXaaRUnok+kx1WdO15EQc=",
    version = "v2.1.0+incompatible",
)

go_repository(
    name = "com_github_chzyer_test",
    importpath = "github.com/chzyer/test",
    sum = "h1:q763qf9huN11kDQavWsoZXJNW3xEE4JJyHa5Q25/sd8=",
    version = "v0.0.0-20180213035817-a1ea475d72b1",
)

go_repository(
    name = "com_github_client9_misspell",
    importpath = "github.com/client9/misspell",
    sum = "h1:ta993UF76GwbvJcIo3Y68y/M3WxlpEHPWIGDkJYwzJI=",
    version = "v0.3.4",
)

go_repository(
    name = "com_github_cncf_udpa_go",
    importpath = "github.com/cncf/udpa/go",
    sum = "h1:hzAQntlaYRkVSFEfj9OTWlVV1H155FMD8BTKktLv0QI=",
    version = "v0.0.0-20210930031921-04548b0d99d4",
)

go_repository(
    name = "com_github_envoyproxy_go_control_plane",
    importpath = "github.com/envoyproxy/go-control-plane",
    sum = "h1:fP+fF0up6oPY49OrjPrhIJ8yQfdIM85NXMLkMg1EXVs=",
    version = "v0.9.10-0.20210907150352-cf90f659a021",
)

go_repository(
    name = "com_github_envoyproxy_protoc_gen_validate",
    importpath = "github.com/envoyproxy/protoc-gen-validate",
    sum = "h1:EQciDnbrYxy13PgWoY8AqoxGiPrpgBZ1R8UNe3ddc+A=",
    version = "v0.1.0",
)

go_repository(
    name = "com_github_go_gl_glfw",
    importpath = "github.com/go-gl/glfw",
    sum = "h1:QbL/5oDUmRBzO9/Z7Seo6zf912W/a6Sr4Eu0G/3Jho0=",
    version = "v0.0.0-20190409004039-e6da0acd62b1",
)

go_repository(
    name = "com_github_go_gl_glfw_v3_3_glfw",
    importpath = "github.com/go-gl/glfw/v3.3/glfw",
    sum = "h1:WtGNWLvXpe6ZudgnXrq0barxBImvnnJoMEhXAzcbM0I=",
    version = "v0.0.0-20200222043503-6f7a984d4dc4",
)

go_repository(
    name = "com_github_golang_glog",
    importpath = "github.com/golang/glog",
    sum = "h1:VKtxabqXZkF25pY9ekfRL6a582T4P37/31XEstQ5p58=",
    version = "v0.0.0-20160126235308-23def4e6c14b",
)

go_repository(
    name = "com_github_golang_groupcache",
    importpath = "github.com/golang/groupcache",
    sum = "h1:oI5xCqsCo564l8iNU+DwB5epxmsaqB+rhGL0m5jtYqE=",
    version = "v0.0.0-20210331224755-41bb18bfe9da",
)

go_repository(
    name = "com_github_golang_protobuf",
    importpath = "github.com/golang/protobuf",
    sum = "h1:+Z5KGCizgyZCbGh1KZqA0fcLLkwbsjIzS4aV2v7wJX0=",
    version = "v1.4.2",
)

go_repository(
    name = "com_github_google_btree",
    importpath = "github.com/google/btree",
    sum = "h1:0udJVsspx3VBr5FwtLhQQtuAsVc79tTq0ocGIPAU6qo=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_google_go_cmp",
    importpath = "github.com/google/go-cmp",
    sum = "h1:Khx7svrCpmxxtHBq5j2mp/xVjsi8hQMfNLvJFAlrGgU=",
    version = "v0.5.5",
)

go_repository(
    name = "com_github_google_martian",
    importpath = "github.com/google/martian",
    sum = "h1:/CP5g8u/VJHijgedC/Legn3BAbAaWPgecwXBIDzw5no=",
    version = "v2.1.0+incompatible",
)

go_repository(
    name = "com_github_google_pprof",
    importpath = "github.com/google/pprof",
    sum = "h1:K6RDEckDVWvDI9JAJYCmNdQXq6neHJOYx3V6jnqNEec=",
    version = "v0.0.0-20210720184732-4bb14d4b1be1",
)

go_repository(
    name = "com_github_googleapis_gax_go_v2",
    importpath = "github.com/googleapis/gax-go/v2",
    sum = "h1:dp3bWCh+PPO1zjRRiCSczJav13sBvG4UhNyVTa1KqdU=",
    version = "v2.1.1",
)

go_repository(
    name = "com_github_hashicorp_golang_lru",
    importpath = "github.com/hashicorp/golang-lru",
    sum = "h1:YDjusn29QI/Das2iO9M0BHnIbxPeyuCHsjMW+lJfyTc=",
    version = "v0.5.4",
)

go_repository(
    name = "com_github_ianlancetaylor_demangle",
    importpath = "github.com/ianlancetaylor/demangle",
    sum = "h1:mV02weKRL81bEnm8A0HT1/CAelMQDBuQIfLw8n+d6xI=",
    version = "v0.0.0-20200824232613-28f6c0f3b639",
)

go_repository(
    name = "com_github_jmespath_go_jmespath",
    importpath = "github.com/jmespath/go-jmespath",
    sum = "h1:BEgLn5cpjn8UN1mAw4NjwDrS35OdebyEtFe+9YPoQUg=",
    version = "v0.4.0",
)

go_repository(
    name = "com_github_jstemmer_go_junit_report",
    importpath = "github.com/jstemmer/go-junit-report",
    sum = "h1:6QPYqodiu3GuPL+7mfx+NwDdp2eTkp9IfEUpgAwUN0o=",
    version = "v0.9.1",
)

go_repository(
    name = "com_github_prometheus_client_model",
    importpath = "github.com/prometheus/client_model",
    sum = "h1:uq5h0d+GuxiXLJLNABMgp2qUWDPiLvgCzz2dUR+/W/M=",
    version = "v0.2.0",
)

go_repository(
    name = "com_github_yuin_goldmark",
    importpath = "github.com/yuin/goldmark",
    sum = "h1:/vn0k+RBvwlxEmP5E7SZMqNxPhfMVFEJiykr15/0XKM=",
    version = "v1.4.1",
)

go_repository(
    name = "com_google_cloud_go",
    importpath = "cloud.google.com/go",
    sum = "h1:t9Iw5QH5v4XtlEQaCtUY7x6sCABps8sW0acw7e2WQ6Y=",
    version = "v0.100.2",
)

go_repository(
    name = "com_google_cloud_go_bigquery",
    importpath = "cloud.google.com/go/bigquery",
    sum = "h1:PQcPefKFdaIzjQFbiyOgAqyx8q5djaE7x9Sqe712DPA=",
    version = "v1.8.0",
)

go_repository(
    name = "com_google_cloud_go_datastore",
    importpath = "cloud.google.com/go/datastore",
    sum = "h1:/May9ojXjRkPBNVrq+oWLqmWCkr4OU5uRY29bu0mRyQ=",
    version = "v1.1.0",
)

go_repository(
    name = "com_google_cloud_go_pubsub",
    importpath = "cloud.google.com/go/pubsub",
    sum = "h1:ukjixP1wl0LpnZ6LWtZJ0mX5tBmjp1f8Sqer8Z2OMUU=",
    version = "v1.3.1",
)

go_repository(
    name = "com_google_cloud_go_storage",
    importpath = "cloud.google.com/go/storage",
    sum = "h1:STgFzyU5/8miMl0//zKh2aQeTyeaUH3WN9bSUiJ09bA=",
    version = "v1.10.0",
)

go_repository(
    name = "com_shuralyov_dmitri_gpu_mtl",
    importpath = "dmitri.shuralyov.com/gpu/mtl",
    sum = "h1:VpgP7xuJadIUuKccphEpTJnWhS2jkQyMt6Y7pJCD7fY=",
    version = "v0.0.0-20190408044501-666a987793e9",
)

go_repository(
    name = "io_opencensus_go",
    importpath = "go.opencensus.io",
    sum = "h1:gqCw0LfLxScz8irSi8exQc7fyQ0fKQU/qnC/X8+V/1M=",
    version = "v0.23.0",
)

go_repository(
    name = "io_rsc_binaryregexp",
    importpath = "rsc.io/binaryregexp",
    sum = "h1:HfqmD5MEmC0zvwBuF187nq9mdnXjXsSivRiXN7SmRkE=",
    version = "v0.2.0",
)

go_repository(
    name = "io_rsc_quote_v3",
    importpath = "rsc.io/quote/v3",
    sum = "h1:9JKUTTIUgS6kzR9mK1YuGKv6Nl+DijDNIc0ghT58FaY=",
    version = "v3.1.0",
)

go_repository(
    name = "io_rsc_sampler",
    importpath = "rsc.io/sampler",
    sum = "h1:7uVkIFmeBqHfdjD+gZwtXXI+RODJ2Wc4O7MPEh/QiW4=",
    version = "v1.3.0",
)

go_repository(
    name = "org_golang_google_api",
    importpath = "google.golang.org/api",
    sum = "h1:MTW9c+LIBAbwoS1Gb+YV7NjFBt2f7GtAS5hIzh2NjgQ=",
    version = "v0.65.0",
)

go_repository(
    name = "org_golang_google_appengine",
    importpath = "google.golang.org/appengine",
    sum = "h1:FZR1q0exgwxzPzp/aF+VccGrSfxfPpkBqjIIEq3ru6c=",
    version = "v1.6.7",
)

go_repository(
    name = "org_golang_google_genproto",
    importpath = "google.golang.org/genproto",
    sum = "h1:aCsSLXylHWFno0r4S3joLpiaWayvqd2Mn4iSvx4WZZc=",
    version = "v0.0.0-20220114231437-d2e6a121cae0",
)

go_repository(
    name = "org_golang_google_grpc",
    importpath = "google.golang.org/grpc",
    sum = "h1:LAv2ds7cmFV/XTS3XG1NneeENYrXGmorPxsBbptIjNc=",
    version = "v1.53.0",
)

go_repository(
    name = "org_golang_x_exp",
    importpath = "golang.org/x/exp",
    sum = "h1:FR+oGxGfbQu1d+jglI3rCkjAjUnhRSZcUxr+DqlDLNo=",
    version = "v0.0.0-20200331195152-e8c3332aa8e5",
)

go_repository(
    name = "org_golang_x_image",
    importpath = "golang.org/x/image",
    sum = "h1:+qEpEAPhDZ1o0x3tHzZTQDArnOixOzGD9HUJfcg0mb4=",
    version = "v0.0.0-20190802002840-cff245a6509b",
)

go_repository(
    name = "org_golang_x_mobile",
    importpath = "golang.org/x/mobile",
    sum = "h1:4+4C/Iv2U4fMZBiMCc98MG1In4gJY5YRhtpDNeDeHWs=",
    version = "v0.0.0-20190719004257-d2bd2a29d028",
)

go_repository(
    name = "org_golang_x_net",
    importpath = "golang.org/x/net",
    sum = "h1:HVyaeDAYux4pnY+D/SiwmLOR36ewZ4iGQIIrtnuCjFA=",
    version = "v0.0.0-20220425223048-2871e0cb64e4",
)

go_repository(
    name = "org_golang_x_oauth2",
    importpath = "golang.org/x/oauth2",
    sum = "h1:RerP+noqYHUQ8CMRcPlC2nvTa4dcBIjegkuWdcUDuqg=",
    version = "v0.0.0-20211104180415-d3ed0bb246c8",
)

go_repository(
    name = "org_golang_x_sync",
    importpath = "golang.org/x/sync",
    sum = "h1:5KslGYwFpkhGh+Q16bwMP3cOontH8FOep7tGV86Y7SQ=",
    version = "v0.0.0-20210220032951-036812b2e83c",
)

go_repository(
    name = "org_golang_x_time",
    importpath = "golang.org/x/time",
    sum = "h1:Hir2P/De0WpUhtrKGGjvSb2YxUgyZ7EFOSLIcSSpiwE=",
    version = "v0.0.0-20201208040808-7e3f01d25324",
)

go_repository(
    name = "com_github_hashicorp_errwrap",
    importpath = "github.com/hashicorp/errwrap",
    sum = "h1:hLrqtEDnRye3+sgx6z4qVLNuviH3MR5aQ0ykNJa/UYA=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_hashicorp_go_multierror",
    importpath = "github.com/hashicorp/go-multierror",
    sum = "h1:B9UzwGQJehnUY1yNrnwREHc3fGbC2xefo8g4TbElacI=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_cenkalti_backoff_v4",
    importpath = "github.com/cenkalti/backoff/v4",
    sum = "h1:c8LkOFQTzuO0WBM/ae5HdGQuZPfPxp7lqBRwQRm4fSc=",
    version = "v4.1.0",
)

go_repository(
    name = "com_github_dustin_go_humanize",
    importpath = "github.com/dustin/go-humanize",
    sum = "h1:VSnTsYCnlFHaM2/igO1h6X3HA71jcobQuxemgkq4zYo=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_google_gofuzz",
    importpath = "github.com/google/gofuzz",
    sum = "h1:Hsa8mG0dQ46ij8Sl2AYJDUv1oA9/d6Vk+3LG99Oe02g=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_gopherjs_gopherjs",
    importpath = "github.com/gopherjs/gopherjs",
    sum = "h1:EGx4pi6eqNxGaHF6qqu48+N2wcFQ5qg5FXgOdqsJ5d8=",
    version = "v0.0.0-20181017120253-0766667cb4d1",
)

go_repository(
    name = "com_github_json_iterator_go",
    importpath = "github.com/json-iterator/go",
    sum = "h1:Kz6Cvnvv2wGdaG/V8yMvfkmNiXq9Ya2KUv4rouJJr68=",
    version = "v1.1.10",
)

go_repository(
    name = "com_github_jtolds_gls",
    importpath = "github.com/jtolds/gls",
    sum = "h1:xdiiI2gbIgH/gLH7ADydsJ1uDOEzR8yvV7C0MuV77Wo=",
    version = "v4.20.0+incompatible",
)

go_repository(
    name = "com_github_klauspost_cpuid",
    importpath = "github.com/klauspost/cpuid",
    sum = "h1:CCtW0xUnWGVINKvE/WWOYKdsPV6mawAtvQuSl8guwQs=",
    version = "v1.2.3",
)

go_repository(
    name = "com_github_konsorten_go_windows_terminal_sequences",
    importpath = "github.com/konsorten/go-windows-terminal-sequences",
    sum = "h1:CE8S1cTafDpPvMhIxNJKvHsGVBgn1xWYf1NbHQhywc8=",
    version = "v1.0.3",
)

go_repository(
    name = "com_github_minio_md5_simd",
    importpath = "github.com/minio/md5-simd",
    sum = "h1:QPfiOqlZH+Cj9teu0t9b1nTBfPbyTl16Of5MeuShdK4=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_minio_minio_go_v6",
    importpath = "github.com/minio/minio-go/v6",
    sum = "h1:ixPkbKkyD7IhnluRgQpGSpHdpvNVaW6OD5R9IAO/9Tw=",
    version = "v6.0.57",
)

go_repository(
    name = "com_github_minio_sha256_simd",
    importpath = "github.com/minio/sha256-simd",
    sum = "h1:5QHSlgo3nt5yKOJrC7W8w7X+NFl8cMPZm96iu8kKUJU=",
    version = "v0.1.1",
)

go_repository(
    name = "com_github_mitchellh_go_homedir",
    importpath = "github.com/mitchellh/go-homedir",
    sum = "h1:lukF9ziXFxDFPkA1vsr5zpc1XuPDn/wFntq5mG+4E0Y=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_modern_go_concurrent",
    importpath = "github.com/modern-go/concurrent",
    sum = "h1:TRLaZ9cD/w8PVh93nsPXa1VrQ6jlwL5oN8l14QlcNfg=",
    version = "v0.0.0-20180306012644-bacd9c7ef1dd",
)

go_repository(
    name = "com_github_modern_go_reflect2",
    importpath = "github.com/modern-go/reflect2",
    sum = "h1:9f412s+6RmYXLWZSEzVVgPGK7C2PphHj5RJrvfx9AWI=",
    version = "v1.0.1",
)

go_repository(
    name = "com_github_sirupsen_logrus",
    importpath = "github.com/sirupsen/logrus",
    sum = "h1:dJKuHgqk1NNQlqoA6BTlM1Wf9DOH3NBjQyu0h9+AZZE=",
    version = "v1.8.1",
)

go_repository(
    name = "com_github_smartystreets_assertions",
    importpath = "github.com/smartystreets/assertions",
    sum = "h1:zE9ykElWQ6/NYmHa3jpm/yHnI4xSofP+UP6SpjHcSeM=",
    version = "v0.0.0-20180927180507-b2de0cb4f26d",
)

go_repository(
    name = "com_github_smartystreets_goconvey",
    importpath = "github.com/smartystreets/goconvey",
    sum = "h1:fv0U8FUIMPNf1L9lnHLvLhgicrIVChEkdzIKYqbNC9s=",
    version = "v1.6.4",
)

go_repository(
    name = "in_gopkg_ini_v1",
    importpath = "gopkg.in/ini.v1",
    sum = "h1:7N3gPTt50s8GuLortA00n8AqRTk75qOP98+mTPpgzRk=",
    version = "v1.42.0",
)

go_repository(
    name = "com_github_cespare_xxhash",
    importpath = "github.com/cespare/xxhash",
    sum = "h1:a6HrQnmkObjyL+Gs60czilIUGqrzKutQD6XZog3p+ko=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_oneofone_xxhash",
    importpath = "github.com/OneOfOne/xxhash",
    sum = "h1:KMrpdQIwFcEqXDklaen+P1axHaj9BSKzvpUUfnHldSE=",
    version = "v1.2.2",
)

go_repository(
    name = "com_github_spaolacci_murmur3",
    importpath = "github.com/spaolacci/murmur3",
    sum = "h1:qLC7fQah7D6K1B0ujays3HV9gkFtllcxhzImRR7ArPQ=",
    version = "v0.0.0-20180118202830-f09979ecbc72",
)

go_repository(
    name = "com_github_gofrs_uuid",
    importpath = "github.com/gofrs/uuid",
    sum = "h1:yyYWMnhkhrKwwr8gAOcOCYxOOscHgDS9yZgBrnJfGa0=",
    version = "v4.2.0+incompatible",
)

go_repository(
    name = "com_github_go_kit_kit",
    importpath = "github.com/go-kit/kit",
    sum = "h1:dXFJfIHVvUcpSgDOV+Ne6t7jXri8Tfv2uOLHUZ2XNuo=",
    version = "v0.10.0",
)

go_repository(
    name = "com_github_go_logfmt_logfmt",
    importpath = "github.com/go-logfmt/logfmt",
    sum = "h1:TrB8swr/68K7m9CcGut2g3UOihhbcbiMAYiuTXdEih4=",
    version = "v0.5.0",
)

go_repository(
    name = "com_github_go_stack_stack",
    importpath = "github.com/go-stack/stack",
    sum = "h1:5SgMzNM5HxrEjV0ww2lTmX6E2Izsfxas4+YHWRs3Lsk=",
    version = "v1.8.0",
)

go_repository(
    name = "com_github_gogo_protobuf",
    importpath = "github.com/gogo/protobuf",
    sum = "h1:DqDEcV5aeaTmdFBePNpYsp3FlcVH/2ISVVM9Qf8PSls=",
    version = "v1.3.1",
)

go_repository(
    name = "com_github_grpc_ecosystem_go_grpc_middleware",
    importpath = "github.com/grpc-ecosystem/go-grpc-middleware",
    sum = "h1:V59tBiPuMkySHwJkuq/OYkK0WnOLwCwD3UkTbEMr12U=",
    version = "v1.2.1",
)

go_repository(
    name = "com_github_kisielk_errcheck",
    importpath = "github.com/kisielk/errcheck",
    sum = "h1:reN85Pxc5larApoH1keMBiu2GWtPqXQ1nc9gx+jOU+E=",
    version = "v1.2.0",
)

go_repository(
    name = "com_github_kr_logfmt",
    importpath = "github.com/kr/logfmt",
    sum = "h1:T+h1c/A9Gawja4Y9mFVWj2vyii2bbUNDw3kt9VxK2EY=",
    version = "v0.0.0-20140226030751-b84e30acd515",
)

go_repository(
    name = "com_github_hpcloud_tail",
    importpath = "github.com/hpcloud/tail",
    sum = "h1:nfCOvKYfkgYP8hkirhJocXT2+zOD8yUNjXaWfTlyFKI=",
    version = "v1.0.0",
)

go_repository(
    name = "in_gopkg_fsnotify_v1",
    importpath = "gopkg.in/fsnotify.v1",
    sum = "h1:xOHLXZwVvI9hhs+cLKq5+I5onOuwQLhQwiu63xxlHs4=",
    version = "v1.4.7",
)

go_repository(
    name = "in_gopkg_tomb_v1",
    importpath = "gopkg.in/tomb.v1",
    sum = "h1:uRGJdciOHaEIrze2W8Q3AKkepLTh2hOroT7a+7czfdQ=",
    version = "v1.0.0-20141024135613-dd632973f1e7",
)

go_repository(
    name = "com_github_drone_go_scm",
    importpath = "github.com/drone/go-scm",
    sum = "h1:9ZrokpIAX+BANYGahqJp6vJqgYiD5c8v5A4iVk+oyZo=",
    version = "v1.29.1",
)

go_repository(
    name = "com_github_drone_go_scm_codecommit",
    importpath = "github.com/drone/go-scm-codecommit",
    sum = "h1:0i0YKhLdfnUvtB7UZstfbDslmhh1DXR+z3JsrNSwsG4=",
    version = "v0.0.0-20210315104920-2d8b9dc5ed8a",
)

go_repository(
    name = "com_github_h2non_gock",
    importpath = "github.com/h2non/gock",
    sum = "h1:17gCehSo8ZOgEsFKpQgqHiR7VLyjxdAG3lkhVvO9QZU=",
    version = "v1.0.9",
)

go_repository(
    name = "com_github_gertd_go_pluralize",
    importpath = "github.com/gertd/go-pluralize",
    sum = "h1:RgvJTJ5W7olOoAks97BOwOlekBFsLEyh00W48Z6ZEZY=",
    version = "v0.1.7",
)

go_repository(
    name = "com_github_alecthomas_template",
    importpath = "github.com/alecthomas/template",
    sum = "h1:JYp7IbQjafoB+tBA3gMyHYHrpOtNuDiK/uB5uXxq5wM=",
    version = "v0.0.0-20190718012654-fb15b899a751",
)

go_repository(
    name = "com_github_alecthomas_units",
    importpath = "github.com/alecthomas/units",
    sum = "h1:UQZhZ2O0vMHr2cI+DC1Mbh0TJxzA3RcLoMsFw+aXw7E=",
    version = "v0.0.0-20190924025748-f65c72e2690d",
)

go_repository(
    name = "com_github_go_chi_chi",
    importpath = "github.com/go-chi/chi",
    sum = "h1:kfTK3Cxd/dkMu/rKs5ZceWYp+t5CtiE7vmaTv3LjC6w=",
    version = "v1.5.1",
)

go_repository(
    name = "com_github_joho_godotenv",
    importpath = "github.com/joho/godotenv",
    sum = "h1:3l4+N6zfMWnkbPEXKng2o2/MR5mSwTrBih4ZEkkz1lg=",
    version = "v1.4.0",
)

go_repository(
    name = "com_github_kelseyhightower_envconfig",
    importpath = "github.com/kelseyhightower/envconfig",
    sum = "h1:Im6hONhd3pLkfDFsbRgu68RDNkGF1r3dvMUtDTo2cv8=",
    version = "v1.4.0",
)

go_repository(
    name = "in_gopkg_alecthomas_kingpin_v2",
    importpath = "gopkg.in/alecthomas/kingpin.v2",
    sum = "h1:jMFz6MfLP0/4fUyZle81rXUoxOBFi19VUFKVDOQfozc=",
    version = "v2.2.6",
)

go_repository(
    name = "io_etcd_go_bbolt",
    importpath = "go.etcd.io/bbolt",
    sum = "h1:MUGmc65QhB3pIlaQ5bB4LwqSj6GIonVJXpZiaKNyaKk=",
    version = "v1.3.3",
)

go_repository(
    name = "com_github_cenkalti_backoff",
    importpath = "github.com/cenkalti/backoff",
    sum = "h1:tNowT99t7UNflLxfYYSlKYsBpXdEet03Pg2g16Swow4=",
    version = "v2.2.1+incompatible",
)

go_repository(
    name = "ag_pack_amqp",
    importpath = "pack.ag/amqp",
    sum = "h1:cuNDWLUTbKRtEZwhB0WQBXf9pGbm87pUBXQhvcFxBWg=",
    version = "v0.11.2",
)

go_repository(
    name = "cc_mvdan_interfacer",
    importpath = "mvdan.cc/interfacer",
    sum = "h1:WX1yoOaKQfddO/mLzdV4wptyWgoH/6hwLs7QHTixo0I=",
    version = "v0.0.0-20180901003855-c20040233aed",
)

go_repository(
    name = "cc_mvdan_lint",
    importpath = "mvdan.cc/lint",
    sum = "h1:DxJ5nJdkhDlLok9K6qO+5290kphDJbHOQO1DFFFTeBo=",
    version = "v0.0.0-20170908181259-adc824a0674b",
)

go_repository(
    name = "cc_mvdan_unparam",
    importpath = "mvdan.cc/unparam",
    sum = "h1:kAREL6MPwpsk1/PQPFD3Eg7WAQR5mPTWZJaBiG5LDbY=",
    version = "v0.0.0-20200501210554-b37ab49443f7",
)

go_repository(
    name = "com_github_alecthomas_kingpin",
    importpath = "github.com/alecthomas/kingpin",
    sum = "h1:5svnBTFgJjZvGKyYBtMB0+m5wvrbUHiqye8wRJMlnYI=",
    version = "v2.2.6+incompatible",
)

go_repository(
    name = "com_github_apex_log",
    importpath = "github.com/apex/log",
    sum = "h1:1fyfbPvUwD10nMoh3hY6MXzvZShJQn9/ck7ATgAt5pA=",
    version = "v1.3.0",
)

go_repository(
    name = "com_github_apex_logs",
    importpath = "github.com/apex/logs",
    sum = "h1:KmEBVwfDUOTFcBO8cfkJYwdQ5487UZSN+GteOGPmiro=",
    version = "v0.0.4",
)

go_repository(
    name = "com_github_aphistic_golf",
    importpath = "github.com/aphistic/golf",
    sum = "h1:2KLQMJ8msqoPHIPDufkxVcoTtcmE5+1sL9950m4R9Pk=",
    version = "v0.0.0-20180712155816-02c07f170c5a",
)

go_repository(
    name = "com_github_aphistic_sweet",
    importpath = "github.com/aphistic/sweet",
    sum = "h1:I4z+fAUqvKfvZV/CHi5dV0QuwbmIvYYFDjG0Ss5QpAs=",
    version = "v0.2.0",
)

go_repository(
    name = "com_github_armon_circbuf",
    importpath = "github.com/armon/circbuf",
    sum = "h1:QEF07wC0T1rKkctt1RINW/+RMTVmiwxETico2l3gxJA=",
    version = "v0.0.0-20150827004946-bbbad097214e",
)

go_repository(
    name = "com_github_armon_consul_api",
    importpath = "github.com/armon/consul-api",
    sum = "h1:G1bPvciwNyF7IUmKXNt9Ak3m6u9DE1rF+RmtIkBpVdA=",
    version = "v0.0.0-20180202201655-eb2c6b5be1b6",
)

go_repository(
    name = "com_github_armon_go_metrics",
    importpath = "github.com/armon/go-metrics",
    sum = "h1:8GUt8eRujhVEGZFFEjBj46YV4rDjvGrNxb0KMWYkL2I=",
    version = "v0.0.0-20180917152333-f0300d1749da",
)

go_repository(
    name = "com_github_armon_go_radix",
    importpath = "github.com/armon/go-radix",
    sum = "h1:BUAU3CGlLvorLI26FmByPp2eC2qla6E1Tw+scpcg/to=",
    version = "v0.0.0-20180808171621-7fddfc383310",
)

go_repository(
    name = "com_github_aybabtme_rgbterm",
    importpath = "github.com/aybabtme/rgbterm",
    sum = "h1:WWB576BN5zNSZc/M9d/10pqEx5VHNhaQ/yOVAkmj5Yo=",
    version = "v0.0.0-20170906152045-cc83f3b3ce59",
)

go_repository(
    name = "com_github_azure_azure_amqp_common_go_v2",
    importpath = "github.com/Azure/azure-amqp-common-go/v2",
    sum = "h1:+QbFgmWCnPzdaRMfsI0Yb6GrRdBj5jVL8N3EXuEUcBQ=",
    version = "v2.1.0",
)

go_repository(
    name = "com_github_azure_azure_pipeline_go",
    importpath = "github.com/Azure/azure-pipeline-go",
    sum = "h1:6oiIS9yaG6XCCzhgAgKFfIWyo4LLCiDhZot6ltoThhY=",
    version = "v0.2.2",
)

go_repository(
    name = "com_github_azure_azure_sdk_for_go",
    importpath = "github.com/Azure/azure-sdk-for-go",
    sum = "h1:PAHkmPqd/vQV4LJcqzEUM1elCyTMWjbrO8oFMl0dvBE=",
    version = "v42.3.0+incompatible",
)

go_repository(
    name = "com_github_azure_azure_service_bus_go",
    importpath = "github.com/Azure/azure-service-bus-go",
    sum = "h1:G1qBLQvHCFDv9pcpgwgFkspzvnGknJRR0PYJ9ytY/JA=",
    version = "v0.9.1",
)

go_repository(
    name = "com_github_azure_azure_storage_blob_go",
    importpath = "github.com/Azure/azure-storage-blob-go",
    sum = "h1:53qhf0Oxa0nOjgbDeeYPUeyiNmafAFEY95rZLK0Tj6o=",
    version = "v0.8.0",
)

go_repository(
    name = "com_github_azure_go_ansiterm",
    importpath = "github.com/Azure/go-ansiterm",
    sum = "h1:w+iIsaOQNcT7OZ575w+acHgRric5iCyQh+xv+KJ4HB8=",
    version = "v0.0.0-20170929234023-d6e3b3328b78",
)

go_repository(
    name = "com_github_azure_go_autorest",
    importpath = "github.com/Azure/go-autorest",
    sum = "h1:V5VMDjClD3GiElqLWO7mz2MxNAK/vTfRHdAubSIPRgs=",
    version = "v14.2.0+incompatible",
)

go_repository(
    name = "com_github_azure_go_autorest_autorest",
    importpath = "github.com/Azure/go-autorest/autorest",
    sum = "h1:NuSF3gXetiHyUbVdneJMEVyPUYAe5wh+aN08JYAf1tI=",
    version = "v0.10.2",
)

go_repository(
    name = "com_github_azure_go_autorest_autorest_adal",
    importpath = "github.com/Azure/go-autorest/autorest/adal",
    sum = "h1:Y3bBUV4rTuxenJJs41HU3qmqsb+auo+a3Lz+PlJPpL0=",
    version = "v0.9.5",
)

go_repository(
    name = "com_github_azure_go_autorest_autorest_azure_auth",
    importpath = "github.com/Azure/go-autorest/autorest/azure/auth",
    sum = "h1:iM6UAvjR97ZIeR93qTcwpKNMpV+/FTWjwEbuPD495Tk=",
    version = "v0.4.2",
)

go_repository(
    name = "com_github_azure_go_autorest_autorest_azure_cli",
    importpath = "github.com/Azure/go-autorest/autorest/azure/cli",
    sum = "h1:LXl088ZQlP0SBppGFsRZonW6hSvwgL5gRByMbvUbx8U=",
    version = "v0.3.1",
)

go_repository(
    name = "com_github_azure_go_autorest_autorest_date",
    importpath = "github.com/Azure/go-autorest/autorest/date",
    sum = "h1:7gUk1U5M/CQbp9WoqinNzJar+8KY+LPI6wiWrP/myHw=",
    version = "v0.3.0",
)

go_repository(
    name = "com_github_azure_go_autorest_autorest_mocks",
    importpath = "github.com/Azure/go-autorest/autorest/mocks",
    sum = "h1:K0laFcLE6VLTOwNgSxaGbUcLPuGXlNkbVvq4cW4nIHk=",
    version = "v0.4.1",
)

go_repository(
    name = "com_github_azure_go_autorest_autorest_to",
    importpath = "github.com/Azure/go-autorest/autorest/to",
    sum = "h1:zebkZaadz7+wIQYgC7GXaz3Wb28yKYfVkkBKwc38VF8=",
    version = "v0.3.0",
)

go_repository(
    name = "com_github_azure_go_autorest_autorest_validation",
    importpath = "github.com/Azure/go-autorest/autorest/validation",
    sum = "h1:15vMO4y76dehZSq7pAaOLQxC6dZYsSrj2GQpflyM/L4=",
    version = "v0.2.0",
)

go_repository(
    name = "com_github_azure_go_autorest_logger",
    importpath = "github.com/Azure/go-autorest/logger",
    sum = "h1:ruG4BSDXONFRrZZJ2GUXDiUyVpayPmb1GnWeHDdaNKY=",
    version = "v0.1.0",
)

go_repository(
    name = "com_github_azure_go_autorest_tracing",
    importpath = "github.com/Azure/go-autorest/tracing",
    sum = "h1:TYi4+3m5t6K48TGI9AUdb+IzbnSxvnvUMfuitfgcfuo=",
    version = "v0.6.0",
)

go_repository(
    name = "com_github_beorn7_perks",
    importpath = "github.com/beorn7/perks",
    sum = "h1:VlbKKnNfV8bJzeqoa4cOKqO6bYr3WgKZxO8Z16+hsOM=",
    version = "v1.0.1",
)

go_repository(
    name = "com_github_bgentry_speakeasy",
    importpath = "github.com/bgentry/speakeasy",
    sum = "h1:ByYyxL9InA1OWqxJqqp2A5pYHUrCiAL6K3J+LKSsQkY=",
    version = "v0.1.0",
)

go_repository(
    name = "com_github_bketelsen_crypt",
    importpath = "github.com/bketelsen/crypt",
    sum = "h1:+0HFd5KSZ/mm3JmhmrDukiId5iR6w4+BdFtfSy4yWIc=",
    version = "v0.0.3-0.20200106085610-5cbc8cc4026c",
)

go_repository(
    name = "com_github_blakesmith_ar",
    importpath = "github.com/blakesmith/ar",
    sum = "h1:m935MPodAbYS46DG4pJSv7WO+VECIWUQ7OJYSoTrMh4=",
    version = "v0.0.0-20190502131153-809d4375e1fb",
)

go_repository(
    name = "com_github_blang_semver",
    importpath = "github.com/blang/semver",
    sum = "h1:CGxCgetQ64DKk7rdZ++Vfnb1+ogGNnB17OJKJXD2Cfs=",
    version = "v3.5.0+incompatible",
)

go_repository(
    name = "com_github_bombsimon_wsl_v2",
    importpath = "github.com/bombsimon/wsl/v2",
    sum = "h1:/DdSteYCq4lPX+LqDg7mdoxm14UxzZPoDT0taYc3DTU=",
    version = "v2.2.0",
)

go_repository(
    name = "com_github_bombsimon_wsl_v3",
    importpath = "github.com/bombsimon/wsl/v3",
    sum = "h1:E5SRssoBgtVFPcYWUOFJEcgaySgdtTNYzsSKDOY7ss8=",
    version = "v3.1.0",
)

go_repository(
    name = "com_github_caarlos0_ctrlc",
    importpath = "github.com/caarlos0/ctrlc",
    sum = "h1:2DtF8GSIcajgffDFJzyG15vO+1PuBWOMUdFut7NnXhw=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_campoy_unique",
    importpath = "github.com/campoy/unique",
    sum = "h1:V9a67dfYqPLAvzk5hMQOXYJlZ4SLIXgyKIE+ZiHzgGQ=",
    version = "v0.0.0-20180121183637-88950e537e7e",
)

go_repository(
    name = "com_github_cavaliercoder_go_cpio",
    importpath = "github.com/cavaliercoder/go-cpio",
    sum = "h1:hHg27A0RSSp2Om9lubZpiMgVbvn39bsUmW9U5h0twqc=",
    version = "v0.0.0-20180626203310-925f9528c45e",
)

go_repository(
    name = "com_github_cockroachdb_datadriven",
    importpath = "github.com/cockroachdb/datadriven",
    sum = "h1:OaNxuTZr7kxeODyLWsRMC+OD03aFUH+mW6r2d+MWa5Y=",
    version = "v0.0.0-20190809214429-80d97fb3cbaa",
)

go_repository(
    name = "com_github_containerd_containerd",
    importpath = "github.com/containerd/containerd",
    sum = "h1:xjvXQWABwS2uiv3TWgQt5Uth60Gu86LTGZXMJkjc7rY=",
    version = "v1.3.0",
)

go_repository(
    name = "com_github_coreos_bbolt",
    importpath = "github.com/coreos/bbolt",
    sum = "h1:wZwiHHUieZCquLkDL0B8UhzreNWsPHooDAG3q34zk0s=",
    version = "v1.3.2",
)

go_repository(
    name = "com_github_coreos_etcd",
    importpath = "github.com/coreos/etcd",
    sum = "h1:jFneRYjIvLMLhDLCzuTuU4rSJUjRplcJQ7pD7MnhC04=",
    version = "v3.3.10+incompatible",
)

go_repository(
    name = "com_github_coreos_go_etcd",
    importpath = "github.com/coreos/go-etcd",
    sum = "h1:bXhRBIXoTm9BYHS3gE0TtQuyNZyeEMux2sDi4oo5YOo=",
    version = "v2.0.0+incompatible",
)

go_repository(
    name = "com_github_coreos_go_oidc",
    importpath = "github.com/coreos/go-oidc",
    sum = "h1:sdJrfw8akMnCuUlaZU3tE/uYXFgfqom8DBE9so9EBsM=",
    version = "v2.1.0+incompatible",
)

go_repository(
    name = "com_github_coreos_go_semver",
    importpath = "github.com/coreos/go-semver",
    sum = "h1:wkHLiw0WNATZnSG7epLsujiMCgPAc9xhjJ4tgnAxmfM=",
    version = "v0.3.0",
)

go_repository(
    name = "com_github_coreos_go_systemd",
    importpath = "github.com/coreos/go-systemd",
    sum = "h1:Wf6HqHfScWJN9/ZjdUKyjop4mf3Qdd+1TvvltAvM3m8=",
    version = "v0.0.0-20190321100706-95778dfbb74e",
)

go_repository(
    name = "com_github_coreos_pkg",
    importpath = "github.com/coreos/pkg",
    sum = "h1:lBNOc5arjvs8E5mO2tbpBpLoyyu8B6e44T7hJy6potg=",
    version = "v0.0.0-20180928190104-399ea9e2e55f",
)

go_repository(
    name = "com_github_cpuguy83_go_md2man",
    importpath = "github.com/cpuguy83/go-md2man",
    sum = "h1:BSKMNlYxDvnunlTymqtgONjNnaRV1sTpcovwwjF22jk=",
    version = "v1.0.10",
)

go_repository(
    name = "com_github_cpuguy83_go_md2man_v2",
    importpath = "github.com/cpuguy83/go-md2man/v2",
    sum = "h1:p1EgwI/C7NhT0JmVkwCD2ZBK8j4aeHQX2pMHHBfMQ6w=",
    version = "v2.0.2",
)

go_repository(
    name = "com_github_creack_pty",
    importpath = "github.com/creack/pty",
    sum = "h1:uDmaGzcdjhF4i/plgjmEsriH11Y0o7RKapEf/LDaM3w=",
    version = "v1.1.9",
)

go_repository(
    name = "com_github_devigned_tab",
    importpath = "github.com/devigned/tab",
    sum = "h1:3mD6Kb1mUOYeLpJvTVSDwSg5ZsfSxfvxGRTxRsJsITA=",
    version = "v0.1.1",
)

go_repository(
    name = "com_github_dgryski_go_sip13",
    importpath = "github.com/dgryski/go-sip13",
    sum = "h1:RMLoZVzv4GliuWafOuPuQDKSm1SJph7uCRnnS61JAn4=",
    version = "v0.0.0-20181026042036-e10d5fee7954",
)

go_repository(
    name = "com_github_dimchansky_utfbom",
    importpath = "github.com/dimchansky/utfbom",
    sum = "h1:FcM3g+nofKgUteL8dm/UpdRXNC9KmADgTpLKsu0TRo4=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_djarvur_go_err113",
    importpath = "github.com/Djarvur/go-err113",
    sum = "h1:uCRZZOdMQ0TZPHYTdYpoC0bLYJKPEHPUJ8MeAa51lNU=",
    version = "v0.1.0",
)

go_repository(
    name = "com_github_dnaeon_go_vcr",
    importpath = "github.com/dnaeon/go-vcr",
    sum = "h1:r8L/HqC0Hje5AXMu1ooW8oyQyOFv4GxqpL0nRP7SLLY=",
    version = "v1.0.1",
)

go_repository(
    name = "com_github_docker_cli",
    importpath = "github.com/docker/cli",
    strip_prefix = "cli-a8ff7f821017ae3d5347392c82718d6cba221681",
    type = "zip",
    urls = ["https://github.com/docker/cli/archive/a8ff7f821017ae3d5347392c82718d6cba221681.zip"],
)

go_repository(
    name = "com_github_docker_distribution",
    importpath = "github.com/docker/distribution",
    sum = "h1:a5mlkVzth6W5A4fOsS3D2EO5BUmsJpcB+cRlLU7cSug=",
    version = "v2.7.1+incompatible",
)

go_repository(
    name = "com_github_docker_docker",
    importpath = "github.com/docker/docker",
    sum = "h1:Cvj7S8I4Xpx78KAl6TwTmMHuHlZ/0SM60NUneGJQ7IE=",
    version = "v1.4.2-0.20190924003213-a8608b5b67c7",
)

go_repository(
    name = "com_github_docker_docker_credential_helpers",
    importpath = "github.com/docker/docker-credential-helpers",
    sum = "h1:zI2p9+1NQYdnG6sMU26EX4aVGlqbInSQxQXLvzJ4RPQ=",
    version = "v0.6.3",
)

go_repository(
    name = "com_github_docker_go_connections",
    importpath = "github.com/docker/go-connections",
    sum = "h1:El9xVISelRB7BuFusrZozjnkIM5YnzCViNKohAFqRJQ=",
    version = "v0.4.0",
)

go_repository(
    name = "com_github_docker_go_units",
    importpath = "github.com/docker/go-units",
    sum = "h1:3uh0PgVws3nIA0Q+MwDC8yjEPf9zjRfZZWXZYDct3Tw=",
    version = "v0.4.0",
)

go_repository(
    name = "com_github_docker_spdystream",
    importpath = "github.com/docker/spdystream",
    sum = "h1:cenwrSVm+Z7QLSV/BsnenAOcDXdX4cMv4wP0B/5QbPg=",
    version = "v0.0.0-20160310174837-449fdfce4d96",
)

go_repository(
    name = "com_github_elazarl_goproxy",
    importpath = "github.com/elazarl/goproxy",
    sum = "h1:yUdfgN0XgIJw7foRItutHYUIhlcKzcSf5vDpdhQAKTc=",
    version = "v0.0.0-20180725130230-947c36da3153",
)

go_repository(
    name = "com_github_emicklei_go_restful",
    importpath = "github.com/emicklei/go-restful",
    sum = "h1:spTtZBk5DYEvbxMVutUuTyh1Ao2r4iyvLdACqsl/Ljk=",
    version = "v2.9.5+incompatible",
)

go_repository(
    name = "com_github_evanphx_json_patch",
    importpath = "github.com/evanphx/json-patch",
    sum = "h1:kLcOMZeuLAJvL2BPWLMIj5oaZQobrkAqrL+WFZwQses=",
    version = "v4.9.0+incompatible",
)

go_repository(
    name = "com_github_fatih_color",
    importpath = "github.com/fatih/color",
    sum = "h1:DkWD4oS2D8LGGgTQ6IvwJJXSL5Vp2ffcQg58nFV38Ys=",
    version = "v1.7.0",
)

go_repository(
    name = "com_github_fortytw2_leaktest",
    importpath = "github.com/fortytw2/leaktest",
    sum = "h1:u8491cBMTQ8ft8aeV+adlcytMZylmA5nnwwkRZjI8vw=",
    version = "v1.3.0",
)

go_repository(
    name = "com_github_ghodss_yaml",
    importpath = "github.com/ghodss/yaml",
    sum = "h1:wQHKEahhL6wmXdzwWG11gIVCkOv05bNOh+Rxn0yngAk=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_go_critic_go_critic",
    importpath = "github.com/go-critic/go-critic",
    sum = "h1:sGEEdiuvLV0OC7/yC6MnK3K6LCPBplspK45B0XVdFAc=",
    version = "v0.4.3",
)

go_repository(
    name = "com_github_go_ini_ini",
    importpath = "github.com/go-ini/ini",
    sum = "h1:Mujh4R/dH6YL8bxuISne3xX2+qcQ9p0IxKAP6ExWoUo=",
    version = "v1.25.4",
)

go_repository(
    name = "com_github_go_lintpack_lintpack",
    importpath = "github.com/go-lintpack/lintpack",
    sum = "h1:DI5mA3+eKdWeJ40nU4d6Wc26qmdG8RCi/btYq0TuRN0=",
    version = "v0.5.2",
)

go_repository(
    name = "com_github_go_logr_logr",
    importpath = "github.com/go-logr/logr",
    sum = "h1:QvGt2nLcHH0WK9orKa+ppBPAxREcH364nPUedEpK0TY=",
    version = "v0.2.0",
)

go_repository(
    name = "com_github_go_ole_go_ole",
    importpath = "github.com/go-ole/go-ole",
    sum = "h1:nNBDSCOigTSiarFpYE9J/KtEA1IOW4CNeqT9TQDqCxI=",
    version = "v1.2.4",
)

go_repository(
    name = "com_github_go_openapi_jsonpointer",
    importpath = "github.com/go-openapi/jsonpointer",
    sum = "h1:gZr+CIYByUqjcgeLXnQu2gHYQC9o73G2XUeOFYEICuY=",
    version = "v0.19.5",
)

go_repository(
    name = "com_github_go_openapi_jsonreference",
    importpath = "github.com/go-openapi/jsonreference",
    sum = "h1:UBIxjkht+AWIgYzCDSv2GN+E/togfwXUJFRTWhl2Jjs=",
    version = "v0.19.6",
)

go_repository(
    name = "com_github_go_openapi_spec",
    importpath = "github.com/go-openapi/spec",
    sum = "h1:O8hJrt0UMnhHcluhIdUgCLRWyM2x7QkBXRvOs7m+O1M=",
    version = "v0.20.4",
)

go_repository(
    name = "com_github_go_openapi_swag",
    importpath = "github.com/go-openapi/swag",
    sum = "h1:D2NRCBzS9/pEY3gP9Nl8aDqGUcPFrwG2p+CNFrLyrCM=",
    version = "v0.19.15",
)

go_repository(
    name = "com_github_go_toolsmith_astcast",
    importpath = "github.com/go-toolsmith/astcast",
    sum = "h1:JojxlmI6STnFVG9yOImLeGREv8W2ocNUM+iOhR6jE7g=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_go_toolsmith_astcopy",
    importpath = "github.com/go-toolsmith/astcopy",
    sum = "h1:OMgl1b1MEpjFQ1m5ztEO06rz5CUd3oBv9RF7+DyvdG8=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_go_toolsmith_astequal",
    importpath = "github.com/go-toolsmith/astequal",
    sum = "h1:4zxD8j3JRFNyLN46lodQuqz3xdKSrur7U/sr0SDS/gQ=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_go_toolsmith_astfmt",
    importpath = "github.com/go-toolsmith/astfmt",
    sum = "h1:A0vDDXt+vsvLEdbMFJAUBI/uTbRw1ffOPnxsILnFL6k=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_go_toolsmith_astinfo",
    importpath = "github.com/go-toolsmith/astinfo",
    sum = "h1:wP6mXeB2V/d1P1K7bZ5vDUO3YqEzcvOREOxZPEu3gVI=",
    version = "v0.0.0-20180906194353-9809ff7efb21",
)

go_repository(
    name = "com_github_go_toolsmith_astp",
    importpath = "github.com/go-toolsmith/astp",
    sum = "h1:alXE75TXgcmupDsMK1fRAy0YUzLzqPVvBKoyWV+KPXg=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_go_toolsmith_pkgload",
    importpath = "github.com/go-toolsmith/pkgload",
    sum = "h1:4DFWWMXVfbcN5So1sBNW9+yeiMqLFGl1wFLTL5R0Tgg=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_go_toolsmith_strparse",
    importpath = "github.com/go-toolsmith/strparse",
    sum = "h1:Vcw78DnpCAKlM20kSbAyO4mPfJn/lyYA4BJUDxe2Jb4=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_go_toolsmith_typep",
    importpath = "github.com/go-toolsmith/typep",
    sum = "h1:8xdsa1+FSIH/RhEkgnD1j2CJOy5mNllW1Q9tRiYwvlk=",
    version = "v1.0.2",
)

go_repository(
    name = "com_github_go_xmlfmt_xmlfmt",
    importpath = "github.com/go-xmlfmt/xmlfmt",
    sum = "h1:khEcpUM4yFcxg4/FHQWkvVRmgijNXRfzkIDHh23ggEo=",
    version = "v0.0.0-20191208150333-d5b6f63a941b",
)

go_repository(
    name = "com_github_gobwas_glob",
    importpath = "github.com/gobwas/glob",
    sum = "h1:A4xDbljILXROh+kObIiy5kIaPYD8e96x1tgBhUI5J+Y=",
    version = "v0.2.3",
)

go_repository(
    name = "com_github_gofrs_flock",
    importpath = "github.com/gofrs/flock",
    sum = "h1:ekuhfTjngPhisSjOJ0QWKpPQE8/rbknHaes6WVJj5Hw=",
    version = "v0.0.0-20190320160742-5135e617513b",
)

go_repository(
    name = "com_github_golangci_check",
    importpath = "github.com/golangci/check",
    sum = "h1:23T5iq8rbUYlhpt5DB4XJkc6BU31uODLD1o1gKvZmD0=",
    version = "v0.0.0-20180506172741-cfe4005ccda2",
)

go_repository(
    name = "com_github_golangci_dupl",
    importpath = "github.com/golangci/dupl",
    sum = "h1:w8hkcTqaFpzKqonE9uMCefW1WDie15eSP/4MssdenaM=",
    version = "v0.0.0-20180902072040-3e9179ac440a",
)

go_repository(
    name = "com_github_golangci_errcheck",
    importpath = "github.com/golangci/errcheck",
    sum = "h1:YYWNAGTKWhKpcLLt7aSj/odlKrSrelQwlovBpDuf19w=",
    version = "v0.0.0-20181223084120-ef45e06d44b6",
)

go_repository(
    name = "com_github_golangci_go_misc",
    importpath = "github.com/golangci/go-misc",
    sum = "h1:9kfjN3AdxcbsZBf8NjltjWihK2QfBBBZuv91cMFfDHw=",
    version = "v0.0.0-20180628070357-927a3d87b613",
)

go_repository(
    name = "com_github_golangci_goconst",
    importpath = "github.com/golangci/goconst",
    sum = "h1:pe9JHs3cHHDQgOFXJJdYkK6fLz2PWyYtP4hthoCMvs8=",
    version = "v0.0.0-20180610141641-041c5f2b40f3",
)

go_repository(
    name = "com_github_golangci_gocyclo",
    importpath = "github.com/golangci/gocyclo",
    sum = "h1:pXTK/gkVNs7Zyy7WKgLXmpQ5bHTrq5GDsp8R9Qs67g0=",
    version = "v0.0.0-20180528144436-0a533e8fa43d",
)

go_repository(
    name = "com_github_golangci_gofmt",
    importpath = "github.com/golangci/gofmt",
    sum = "h1:iR3fYXUjHCR97qWS8ch1y9zPNsgXThGwjKPrYfqMPks=",
    version = "v0.0.0-20190930125516-244bba706f1a",
)

go_repository(
    name = "com_github_golangci_golangci_lint",
    importpath = "github.com/golangci/golangci-lint",
    sum = "h1:VYLx63qb+XJsHdZ27PMS2w5JZacN0XG8ffUwe7yQomo=",
    version = "v1.27.0",
)

go_repository(
    name = "com_github_golangci_ineffassign",
    importpath = "github.com/golangci/ineffassign",
    sum = "h1:gLLhTLMk2/SutryVJ6D4VZCU3CUqr8YloG7FPIBWFpI=",
    version = "v0.0.0-20190609212857-42439a7714cc",
)

go_repository(
    name = "com_github_golangci_lint_1",
    importpath = "github.com/golangci/lint-1",
    sum = "h1:MfyDlzVjl1hoaPzPD4Gpb/QgoRfSBR0jdhwGyAWwMSA=",
    version = "v0.0.0-20191013205115-297bf364a8e0",
)

go_repository(
    name = "com_github_golangci_maligned",
    importpath = "github.com/golangci/maligned",
    sum = "h1:kNY3/svz5T29MYHubXix4aDDuE3RWHkPvopM/EDv/MA=",
    version = "v0.0.0-20180506175553-b1d89398deca",
)

go_repository(
    name = "com_github_golangci_misspell",
    importpath = "github.com/golangci/misspell",
    sum = "h1:pLzmVdl3VxTOncgzHcvLOKirdvcx/TydsClUQXTehjo=",
    version = "v0.3.5",
)

go_repository(
    name = "com_github_golangci_prealloc",
    importpath = "github.com/golangci/prealloc",
    sum = "h1:leSNB7iYzLYSSx3J/s5sVf4Drkc68W2wm4Ixh/mr0us=",
    version = "v0.0.0-20180630174525-215b22d4de21",
)

go_repository(
    name = "com_github_golangci_revgrep",
    importpath = "github.com/golangci/revgrep",
    sum = "h1:XQKc8IYQOeRwVs36tDrEmTgDgP88d5iEURwpmtiAlOM=",
    version = "v0.0.0-20180812185044-276a5c0a1039",
)

go_repository(
    name = "com_github_golangci_unconvert",
    importpath = "github.com/golangci/unconvert",
    sum = "h1:zwtduBRr5SSWhqsYNgcuWO2kFlpdOZbP0+yRjmvPGys=",
    version = "v0.0.0-20180507085042-28b1c447d1f4",
)

go_repository(
    name = "com_github_google_go_containerregistry",
    importpath = "github.com/google/go-containerregistry",
    sum = "h1:+vqpHdgIbD7xSeufHJq0iuAx7ILcEeh3fR5Og2nW1R0=",
    version = "v0.3.0",
)

go_repository(
    name = "com_github_google_go_github_v28",
    importpath = "github.com/google/go-github/v28",
    sum = "h1:kORf5ekX5qwXO2mGzXXOjMe/g6ap8ahVe0sBEulhSxo=",
    version = "v28.1.1",
)

go_repository(
    name = "com_github_google_go_querystring",
    importpath = "github.com/google/go-querystring",
    sum = "h1:Xkwi/a1rcvNg1PPYe5vI8GbeBY/jrVuDX5ASuANWTrk=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_google_go_replayers_grpcreplay",
    importpath = "github.com/google/go-replayers/grpcreplay",
    sum = "h1:eNb1y9rZFmY4ax45uEEECSa8fsxGRU+8Bil52ASAwic=",
    version = "v0.1.0",
)

go_repository(
    name = "com_github_google_go_replayers_httpreplay",
    importpath = "github.com/google/go-replayers/httpreplay",
    sum = "h1:AX7FUb4BjrrzNvblr/OlgwrmFiep6soj5K2QSDW7BGk=",
    version = "v0.1.0",
)

go_repository(
    name = "com_github_google_rpmpack",
    importpath = "github.com/google/rpmpack",
    sum = "h1:BW6OvS3kpT5UEPbCZ+KyX/OB4Ks9/MNMhWjqPPkZxsE=",
    version = "v0.0.0-20191226140753-aa36bfddb3a0",
)

go_repository(
    name = "com_github_google_subcommands",
    importpath = "github.com/google/subcommands",
    sum = "h1:/eqq+otEXm5vhfBrbREPCSVQbvofip6kIz+mX5TUH7k=",
    version = "v1.0.1",
)

go_repository(
    name = "com_github_google_uuid",
    importpath = "github.com/google/uuid",
    sum = "h1:EVhdT+1Kseyi1/pUmXKaFxYsDNy9RQYkMWRH68J/W7Y=",
    version = "v1.1.2",
)

go_repository(
    name = "com_github_google_wire",
    importpath = "github.com/google/wire",
    sum = "h1:kXcsA/rIGzJImVqPdhfnr6q0xsS9gU0515q1EPpJ9fE=",
    version = "v0.4.0",
)

go_repository(
    name = "com_github_googleapis_gax_go",
    importpath = "github.com/googleapis/gax-go",
    sum = "h1:silFMLAnr330+NRuag/VjIGF7TLp/LBrV2CJKFLWEww=",
    version = "v2.0.2+incompatible",
)

go_repository(
    name = "com_github_googleapis_gnostic",
    importpath = "github.com/googleapis/gnostic",
    sum = "h1:DLJCy1n/vrD4HPjOvYcT8aYQXpPIzoRZONaYwyycI+I=",
    version = "v0.4.1",
)

go_repository(
    name = "com_github_googlecloudplatform_cloudsql_proxy",
    importpath = "github.com/GoogleCloudPlatform/cloudsql-proxy",
    sum = "h1:sTOp2Ajiew5XIH92YSdwhYc+bgpUX5j5TKK/Ac8Saw8=",
    version = "v0.0.0-20191009163259-e802c2cb94ae",
)

go_repository(
    name = "com_github_googlecloudplatform_k8s_cloud_provider",
    importpath = "github.com/GoogleCloudPlatform/k8s-cloud-provider",
    sum = "h1:N7lSsF+R7wSulUADi36SInSQA3RvfO/XclHQfedr0qk=",
    version = "v0.0.0-20190822182118-27a4ced34534",
)

go_repository(
    name = "com_github_gookit_color",
    importpath = "github.com/gookit/color",
    sum = "h1:xOYBan3Fwlrqj1M1UN2TlHOCRiek3bGzWf/vPnJ1roE=",
    version = "v1.2.4",
)

go_repository(
    name = "com_github_gophercloud_gophercloud",
    importpath = "github.com/gophercloud/gophercloud",
    sum = "h1:P/nh25+rzXouhytV2pUHBb65fnds26Ghl8/391+sT5o=",
    version = "v0.1.0",
)

go_repository(
    name = "com_github_goreleaser_goreleaser",
    importpath = "github.com/goreleaser/goreleaser",
    sum = "h1:Z+7XPrfGK11s/Sp+a06sx2FzGuCjTBdxN2ubpGvQbjY=",
    version = "v0.136.0",
)

go_repository(
    name = "com_github_goreleaser_nfpm",
    importpath = "github.com/goreleaser/nfpm",
    sum = "h1:BPwIomC+e+yuDX9poJowzV7JFVcYA0+LwGSkbAPs2Hw=",
    version = "v1.3.0",
)

go_repository(
    name = "com_github_gorilla_mux",
    importpath = "github.com/gorilla/mux",
    sum = "h1:gnP5JzjVOuiZD07fKKToCAOjS0yOpj/qPETTXCCS6hw=",
    version = "v1.7.3",
)

go_repository(
    name = "com_github_gorilla_websocket",
    importpath = "github.com/gorilla/websocket",
    sum = "h1:q7AeDBpnBk8AogcD4DSag/Ukw/KV+YhzLj2bP5HvKCM=",
    version = "v1.4.1",
)

go_repository(
    name = "com_github_gostaticanalysis_analysisutil",
    importpath = "github.com/gostaticanalysis/analysisutil",
    sum = "h1:iwp+5/UAyzQSFgQ4uR2sni99sJ8Eo9DEacKWM5pekIg=",
    version = "v0.0.3",
)

go_repository(
    name = "com_github_gregjones_httpcache",
    importpath = "github.com/gregjones/httpcache",
    sum = "h1:pdN6V1QBWetyv/0+wjACpqVH+eVULgEjkurDLq3goeM=",
    version = "v0.0.0-20180305231024-9cad4c3443a7",
)

go_repository(
    name = "com_github_grpc_ecosystem_go_grpc_prometheus",
    importpath = "github.com/grpc-ecosystem/go-grpc-prometheus",
    sum = "h1:Ovs26xHkKqVztRpIrF/92BcuyuQ/YW4NSIpoGtfXNho=",
    version = "v1.2.0",
)

go_repository(
    name = "com_github_grpc_ecosystem_grpc_gateway",
    importpath = "github.com/grpc-ecosystem/grpc-gateway",
    sum = "h1:gmcG1KaJ57LophUzW0Hy8NmPhnMZb4M0+kPpLofRdBo=",
    version = "v1.16.0",
)

go_repository(
    name = "com_github_hashicorp_consul_api",
    importpath = "github.com/hashicorp/consul/api",
    sum = "h1:HXNYlRkkM/t+Y/Yhxtwcy02dlYwIaoxzvxPnS+cqy78=",
    version = "v1.3.0",
)

go_repository(
    name = "com_github_hashicorp_consul_sdk",
    importpath = "github.com/hashicorp/consul/sdk",
    sum = "h1:UOxjlb4xVNF93jak1mzzoBatyFju9nrkxpVwIp/QqxQ=",
    version = "v0.3.0",
)

go_repository(
    name = "com_github_hashicorp_go_cleanhttp",
    importpath = "github.com/hashicorp/go-cleanhttp",
    sum = "h1:dH3aiDG9Jvb5r5+bYHsikaOUIpcM0xvgMXVoDkXMzJM=",
    version = "v0.5.1",
)

go_repository(
    name = "com_github_hashicorp_go_hclog",
    importpath = "github.com/hashicorp/go-hclog",
    sum = "h1:CG6TE5H9/JXsFWJCfoIVpKFIkFe6ysEuHirp4DxCsHI=",
    version = "v0.9.2",
)

go_repository(
    name = "com_github_hashicorp_go_immutable_radix",
    importpath = "github.com/hashicorp/go-immutable-radix",
    sum = "h1:AKDB1HM5PWEA7i4nhcpwOrO2byshxBjXVn/J/3+z5/0=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_hashicorp_go_msgpack",
    importpath = "github.com/hashicorp/go-msgpack",
    sum = "h1:zKjpN5BK/P5lMYrLmBHdBULWbJ0XpYR+7NGzqkZzoD4=",
    version = "v0.5.3",
)

go_repository(
    name = "com_github_hashicorp_go_net",
    importpath = "github.com/hashicorp/go.net",
    sum = "h1:sNCoNyDEvN1xa+X0baata4RdcpKwcMS6DH+xwfqPgjw=",
    version = "v0.0.1",
)

go_repository(
    name = "com_github_hashicorp_go_retryablehttp",
    importpath = "github.com/hashicorp/go-retryablehttp",
    sum = "h1:HJunrbHTDDbBb/ay4kxa1n+dLmttUlnP3V9oNE4hmsM=",
    version = "v0.6.6",
)

go_repository(
    name = "com_github_hashicorp_go_rootcerts",
    importpath = "github.com/hashicorp/go-rootcerts",
    sum = "h1:Rqb66Oo1X/eSV1x66xbDccZjhJigjg0+e82kpwzSwCI=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_hashicorp_go_sockaddr",
    importpath = "github.com/hashicorp/go-sockaddr",
    sum = "h1:GeH6tui99pF4NJgfnhp+L6+FfobzVW3Ah46sLo0ICXs=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_hashicorp_go_syslog",
    importpath = "github.com/hashicorp/go-syslog",
    sum = "h1:KaodqZuhUoZereWVIYmpUgZysurB1kBLX2j0MwMrUAE=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_hashicorp_go_uuid",
    importpath = "github.com/hashicorp/go-uuid",
    sum = "h1:fv1ep09latC32wFoVwnqcnKJGnMSdBanPczbHAYm1BE=",
    version = "v1.0.1",
)

go_repository(
    name = "com_github_hashicorp_go_version",
    importpath = "github.com/hashicorp/go-version",
    sum = "h1:3vNe/fWF5CBgRIguda1meWhsZHy3m8gCJ5wx+dIzX/E=",
    version = "v1.2.0",
)

go_repository(
    name = "com_github_hashicorp_hcl",
    importpath = "github.com/hashicorp/hcl",
    sum = "h1:0Anlzjpi4vEasTeNFn2mLJgTSwt0+6sfsiTG8qcWGx4=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_hashicorp_logutils",
    importpath = "github.com/hashicorp/logutils",
    sum = "h1:dLEQVugN8vlakKOUE3ihGLTZJRB4j+M2cdTm/ORI65Y=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_hashicorp_mdns",
    importpath = "github.com/hashicorp/mdns",
    sum = "h1:WhIgCr5a7AaVH6jPUwjtRuuE7/RDufnUvzIr48smyxs=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_hashicorp_memberlist",
    importpath = "github.com/hashicorp/memberlist",
    sum = "h1:EmmoJme1matNzb+hMpDuR/0sbJSUisxyqBGG676r31M=",
    version = "v0.1.3",
)

go_repository(
    name = "com_github_hashicorp_serf",
    importpath = "github.com/hashicorp/serf",
    sum = "h1:YZ7UKsJv+hKjqGVUUbtE3HNj79Eln2oQ75tniF6iPt0=",
    version = "v0.8.2",
)

go_repository(
    name = "com_github_imdario_mergo",
    importpath = "github.com/imdario/mergo",
    sum = "h1:JboBksRwiiAJWvIYJVo46AfV+IAIKZpfrSzVKj42R4Q=",
    version = "v0.3.5",
)

go_repository(
    name = "com_github_inconshreveable_mousetrap",
    importpath = "github.com/inconshreveable/mousetrap",
    sum = "h1:Z8tu5sraLXCXIcARxBp/8cbvlwVa7Z1NHg9XEKhtSvM=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_jarcoal_httpmock",
    importpath = "github.com/jarcoal/httpmock",
    sum = "h1:cHtVEcTxRSX4J0je7mWPfc9BpDpqzXSJ5HbymZmyHck=",
    version = "v1.0.5",
)

go_repository(
    name = "com_github_jingyugao_rowserrcheck",
    importpath = "github.com/jingyugao/rowserrcheck",
    sum = "h1:GmsqmapfzSJkm28dhRoHz2tLRbJmqhU86IPgBtN3mmk=",
    version = "v0.0.0-20191204022205-72ab7603b68a",
)

go_repository(
    name = "com_github_jirfag_go_printf_func_name",
    importpath = "github.com/jirfag/go-printf-func-name",
    sum = "h1:KA9BjwUk7KlCh6S9EAGWBt1oExIUv9WyNCiRz5amv48=",
    version = "v0.0.0-20200119135958-7558a9eaa5af",
)

go_repository(
    name = "com_github_jmoiron_sqlx",
    importpath = "github.com/jmoiron/sqlx",
    sum = "h1:lrdPtrORjGv1HbbEvKWDUAy97mPpFm4B8hp77tcCUJY=",
    version = "v1.2.1-0.20190826204134-d7d95172beb5",
)

go_repository(
    name = "com_github_joefitzgerald_rainbow_reporter",
    importpath = "github.com/joefitzgerald/rainbow-reporter",
    sum = "h1:AuMG652zjdzI0YCCnXAqATtRBpGXMcAnrajcaTrSeuo=",
    version = "v0.1.0",
)

go_repository(
    name = "com_github_jonboulle_clockwork",
    importpath = "github.com/jonboulle/clockwork",
    sum = "h1:VKV+ZcuP6l3yW9doeqz6ziZGgcynBVQO+obU0+0hcPo=",
    version = "v0.1.0",
)

go_repository(
    name = "com_github_jpillora_backoff",
    importpath = "github.com/jpillora/backoff",
    sum = "h1:uvFg412JmmHBHw7iwprIxkPMI+sGQ4kzOWsMeHnm2EA=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_julienschmidt_httprouter",
    importpath = "github.com/julienschmidt/httprouter",
    sum = "h1:U0609e9tgbseu3rBINet9P48AI/D3oJs4dN7jwJOQ1U=",
    version = "v1.3.0",
)

go_repository(
    name = "com_github_klauspost_compress",
    importpath = "github.com/klauspost/compress",
    sum = "h1:U+CaK85mrNNb4k8BNOfgJtJ/gr6kswUCFj6miSzVC6M=",
    version = "v1.9.5",
)

go_repository(
    name = "com_github_lib_pq",
    importpath = "github.com/lib/pq",
    sum = "h1:/qkRGz8zljWiDcFvgpwUpwIAPu3r07TDvs3Rws+o/pU=",
    version = "v1.3.0",
)

go_repository(
    name = "com_github_logrusorgru_aurora",
    importpath = "github.com/logrusorgru/aurora",
    sum = "h1:9MlwzLdW7QSDrhDjFlsEYmxpFyIoXmYRon3dt0io31k=",
    version = "v0.0.0-20181002194514-a7b3b318ed4e",
)

go_repository(
    name = "com_github_magiconair_properties",
    importpath = "github.com/magiconair/properties",
    sum = "h1:LLgXmsheXeRoUOBOjtwPQCWIYqM/LU1ayDtDePerRcY=",
    version = "v1.8.0",
)

go_repository(
    name = "com_github_mailru_easyjson",
    importpath = "github.com/mailru/easyjson",
    sum = "h1:UGYAvKxe3sBsEDzO8ZeWOSlIQfWFlxbzLZe7hwFURr0=",
    version = "v0.7.7",
)

go_repository(
    name = "com_github_maratori_testpackage",
    importpath = "github.com/maratori/testpackage",
    sum = "h1:QtJ5ZjqapShm0w5DosRjg0PRlSdAdlx+W6cCKoALdbQ=",
    version = "v1.0.1",
)

go_repository(
    name = "com_github_masterminds_semver",
    importpath = "github.com/Masterminds/semver",
    sum = "h1:H65muMkzWKEuNDnfl9d70GUjFniHKHRbFPGBuZ3QEww=",
    version = "v1.5.0",
)

go_repository(
    name = "com_github_masterminds_semver_v3",
    importpath = "github.com/Masterminds/semver/v3",
    sum = "h1:Y2lUDsFKVRSYGojLJ1yLxSXdMmMYTYls0rCvoqmMUQk=",
    version = "v3.1.0",
)

go_repository(
    name = "com_github_matoous_godox",
    importpath = "github.com/matoous/godox",
    sum = "h1:RHba4YImhrUVQDHUCe2BNSOz4tVy2yGyXhvYDvxGgeE=",
    version = "v0.0.0-20190911065817-5d6d842e92eb",
)

go_repository(
    name = "com_github_mattn_go_colorable",
    importpath = "github.com/mattn/go-colorable",
    sum = "h1:jF+Du6AlPIjs2BiUiQlKOX0rt3SujHxPnksPKZbaA40=",
    version = "v0.1.12",
)

go_repository(
    name = "com_github_mattn_go_ieproxy",
    importpath = "github.com/mattn/go-ieproxy",
    sum = "h1:qiyop7gCflfhwCzGyeT0gro3sF9AIg9HU98JORTkqfI=",
    version = "v0.0.1",
)

go_repository(
    name = "com_github_mattn_go_isatty",
    importpath = "github.com/mattn/go-isatty",
    sum = "h1:yVuAays6BHfxijgZPzw+3Zlu5yQgKGP2/hcQbHb7S9Y=",
    version = "v0.0.14",
)

go_repository(
    name = "com_github_mattn_go_runewidth",
    importpath = "github.com/mattn/go-runewidth",
    sum = "h1:UnlwIPBGaTZfPQ6T1IGzPI0EkYAQmT9fAEJ/poFC63o=",
    version = "v0.0.2",
)

go_repository(
    name = "com_github_mattn_go_sqlite3",
    importpath = "github.com/mattn/go-sqlite3",
    sum = "h1:pDRiWfl+++eC2FEFRy6jXmQlvp4Yh3z1MJKg4UeYM/4=",
    version = "v1.9.0",
)

go_repository(
    name = "com_github_mattn_go_zglob",
    importpath = "github.com/mattn/go-zglob",
    sum = "h1:xsEx/XUoVlI6yXjqBK062zYhRTZltCNmYPx6v+8DNaY=",
    version = "v0.0.1",
)

go_repository(
    name = "com_github_mattn_goveralls",
    importpath = "github.com/mattn/goveralls",
    sum = "h1:7eJB6EqsPhRVxvwEXGnqdO2sJI0PTsrWoTMXEk9/OQc=",
    version = "v0.0.2",
)

go_repository(
    name = "com_github_matttproud_golang_protobuf_extensions",
    importpath = "github.com/matttproud/golang_protobuf_extensions",
    sum = "h1:4hp9jkHxhMHkqkrB3Ix0jegS5sx/RkqARlsWZ6pIwiU=",
    version = "v1.0.1",
)

go_repository(
    name = "com_github_maxbrunsfeld_counterfeiter_v6",
    importpath = "github.com/maxbrunsfeld/counterfeiter/v6",
    sum = "h1:g+4J5sZg6osfvEfkRZxJ1em0VT95/UOZgi/l7zi1/oE=",
    version = "v6.2.2",
)

go_repository(
    name = "com_github_mgutz_ansi",
    importpath = "github.com/mgutz/ansi",
    sum = "h1:j7+1HpAFS1zy5+Q4qx1fWh90gTKwiN4QCGoY9TWyyO4=",
    version = "v0.0.0-20170206155736-9520e82c474b",
)

go_repository(
    name = "com_github_microsoft_go_winio",
    importpath = "github.com/Microsoft/go-winio",
    sum = "h1:+hMXMk01us9KgxGb7ftKQt2Xpf5hH/yky+TDA+qxleU=",
    version = "v0.4.14",
)

go_repository(
    name = "com_github_miekg_dns",
    importpath = "github.com/miekg/dns",
    sum = "h1:9jZdLNd/P4+SfEJ0TNyxYpsK8N4GtfylBLqtbYN1sbA=",
    version = "v1.0.14",
)

go_repository(
    name = "com_github_mitchellh_cli",
    importpath = "github.com/mitchellh/cli",
    sum = "h1:iGBIsUe3+HZ/AD/Vd7DErOt5sU9fa8Uj7A2s1aggv1Y=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_mitchellh_go_ps",
    importpath = "github.com/mitchellh/go-ps",
    sum = "h1:9+ke9YJ9KGWw5ANXK6ozjoK47uI3uNbXv4YVINBnGm8=",
    version = "v0.0.0-20190716172923-621e5597135b",
)

go_repository(
    name = "com_github_mitchellh_go_testing_interface",
    importpath = "github.com/mitchellh/go-testing-interface",
    sum = "h1:fzU/JVNcaqHQEcVFAKeR41fkiLdIPrefOvVG1VZ96U0=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_mitchellh_gox",
    importpath = "github.com/mitchellh/gox",
    sum = "h1:lfGJxY7ToLJQjHHwi0EX6uYBdK78egf954SQl13PQJc=",
    version = "v0.4.0",
)

go_repository(
    name = "com_github_mitchellh_iochan",
    importpath = "github.com/mitchellh/iochan",
    sum = "h1:C+X3KsSTLFVBr/tK1eYN/vs4rJcvsiLU338UhYPJWeY=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_mitchellh_mapstructure",
    importpath = "github.com/mitchellh/mapstructure",
    sum = "h1:fmNYVwqnSfB9mZU6OS2O6GsXM+wcskZDuKQzvN1EDeE=",
    version = "v1.1.2",
)

go_repository(
    name = "com_github_morikuni_aec",
    importpath = "github.com/morikuni/aec",
    sum = "h1:nP9CBfwrvYnBRgY6qfDQkygYDmYwOilePFkwzv4dU8A=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_mozilla_tls_observatory",
    importpath = "github.com/mozilla/tls-observatory",
    sum = "h1:1xJ+Xi9lYWLaaP4yB67ah0+548CD3110mCPWhVVjFkI=",
    version = "v0.0.0-20200317151703-4fa42e1c2dee",
)

go_repository(
    name = "com_github_munnerz_goautoneg",
    importpath = "github.com/munnerz/goautoneg",
    sum = "h1:C3w9PqII01/Oq1c1nUAm88MOHcQC9l5mIlSMApZMrHA=",
    version = "v0.0.0-20191010083416-a7dc8b61c822",
)

go_repository(
    name = "com_github_mwitkow_go_conntrack",
    importpath = "github.com/mwitkow/go-conntrack",
    sum = "h1:KUppIJq7/+SVif2QVs3tOP0zanoHgBEVAwHxUSIzRqU=",
    version = "v0.0.0-20190716064945-2f068394615f",
)

go_repository(
    name = "com_github_mxk_go_flowrate",
    importpath = "github.com/mxk/go-flowrate",
    sum = "h1:y5//uYreIhSUg3J1GEMiLbxo1LJaP8RfCpH6pymGZus=",
    version = "v0.0.0-20140419014527-cca7078d478f",
)

go_repository(
    name = "com_github_nakabonne_nestif",
    importpath = "github.com/nakabonne/nestif",
    sum = "h1:+yOViDGhg8ygGrmII72nV9B/zGxY188TYpfolntsaPw=",
    version = "v0.3.0",
)

go_repository(
    name = "com_github_nbutton23_zxcvbn_go",
    importpath = "github.com/nbutton23/zxcvbn-go",
    sum = "h1:AREM5mwr4u1ORQBMvzfzBgpsctsbQikCVpvC+tX285E=",
    version = "v0.0.0-20180912185939-ae427f1e4c1d",
)

go_repository(
    name = "com_github_niemeyer_pretty",
    importpath = "github.com/niemeyer/pretty",
    sum = "h1:fD57ERR4JtEqsWbfPhv4DMiApHyliiK5xCTNVSPiaAs=",
    version = "v0.0.0-20200227124842-a10e7caefd8e",
)

go_repository(
    name = "com_github_nytimes_gziphandler",
    importpath = "github.com/NYTimes/gziphandler",
    sum = "h1:lsxEuwrXEAokXB9qhlbKWPpo3KMLZQ5WB5WLQRW1uq0=",
    version = "v0.0.0-20170623195520-56545f4a5d46",
)

go_repository(
    name = "com_github_oklog_ulid",
    importpath = "github.com/oklog/ulid",
    sum = "h1:EGfNDEx6MqHz8B3uNV6QAib1UR2Lm97sHi3ocA6ESJ4=",
    version = "v1.3.1",
)

go_repository(
    name = "com_github_olekukonko_tablewriter",
    importpath = "github.com/olekukonko/tablewriter",
    sum = "h1:58+kh9C6jJVXYjt8IE48G2eWl6BjwU5Gj0gqY84fy78=",
    version = "v0.0.0-20170122224234-a0225b3f23b5",
)

go_repository(
    name = "com_github_onsi_ginkgo",
    importpath = "github.com/onsi/ginkgo",
    sum = "h1:8xi0RTUf59SOSfEtZMvwTvXYMzG4gV23XVHOZiXNtnE=",
    version = "v1.16.5",
)

go_repository(
    name = "com_github_onsi_gomega",
    importpath = "github.com/onsi/gomega",
    sum = "h1:M1GfJqGRrBrrGGsbxzV5dqM2U2ApXefZCQpkukxYRLE=",
    version = "v1.18.1",
)

go_repository(
    name = "com_github_op_go_logging",
    importpath = "github.com/op/go-logging",
    sum = "h1:lDH9UUVJtmYCjyT0CI4q8xvlXPxeZ0gYCVvWbmPlp88=",
    version = "v0.0.0-20160315200505-970db520ece7",
)

go_repository(
    name = "com_github_opencontainers_go_digest",
    importpath = "github.com/opencontainers/go-digest",
    sum = "h1:apOUWs51W5PlhuyGyz9FCeeBIOUDA/6nW8Oi/yOhh5U=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_opencontainers_image_spec",
    importpath = "github.com/opencontainers/image-spec",
    sum = "h1:JMemWkRwHx4Zj+fVxWoMCFm/8sYGGrUVojFA6h/TRcI=",
    version = "v1.0.1",
)

go_repository(
    name = "com_github_openpeedeep_depguard",
    importpath = "github.com/OpenPeeDeeP/depguard",
    sum = "h1:VlW4R6jmBIv3/u1JNlawEvJMM4J+dPORPaZasQee8Us=",
    version = "v1.0.1",
)

go_repository(
    name = "com_github_pascaldekloe_goe",
    importpath = "github.com/pascaldekloe/goe",
    sum = "h1:Lgl0gzECD8GnQ5QCWA8o6BtfL6mDH5rQgM4/fX3avOs=",
    version = "v0.0.0-20180627143212-57f6aae5913c",
)

go_repository(
    name = "com_github_pborman_uuid",
    importpath = "github.com/pborman/uuid",
    sum = "h1:J7Q5mO4ysT1dv8hyrUGHb9+ooztCXu1D8MY8DZYsu3g=",
    version = "v1.2.0",
)

go_repository(
    name = "com_github_peterbourgon_diskv",
    importpath = "github.com/peterbourgon/diskv",
    sum = "h1:UBdAOUP5p4RWqPBg048CAvpKN+vxiaj6gdUUzhl4XmI=",
    version = "v2.0.1+incompatible",
)

go_repository(
    name = "com_github_phayes_checkstyle",
    importpath = "github.com/phayes/checkstyle",
    sum = "h1:CdDQnGF8Nq9ocOS/xlSptM1N3BbrA6/kmaep5ggwaIA=",
    version = "v0.0.0-20170904204023-bfd46e6a821d",
)

go_repository(
    name = "com_github_posener_complete",
    importpath = "github.com/posener/complete",
    sum = "h1:ccV59UEOTzVDnDUEFdT95ZzHVZ+5+158q8+SJb2QV5w=",
    version = "v1.1.1",
)

go_repository(
    name = "com_github_pquerna_cachecontrol",
    importpath = "github.com/pquerna/cachecontrol",
    sum = "h1:0XM1XL/OFFJjXsYXlG30spTkV/E9+gmd5GD1w2HE8xM=",
    version = "v0.0.0-20171018203845-0dec1b30a021",
)

go_repository(
    name = "com_github_prometheus_client_golang",
    importpath = "github.com/prometheus/client_golang",
    sum = "h1:zvJNkoCFAnYFNC24FV8nW4JdRJ3GIFcLbg65lL/JDcw=",
    version = "v1.8.0",
)

go_repository(
    name = "com_github_prometheus_common",
    importpath = "github.com/prometheus/common",
    sum = "h1:RHRyE8UocrbjU+6UvRzwi6HjiDfxrrBU91TtbKzkGp4=",
    version = "v0.14.0",
)

go_repository(
    name = "com_github_prometheus_procfs",
    importpath = "github.com/prometheus/procfs",
    sum = "h1:wH4vA7pcjKuZzjF7lM8awk4fnuJO6idemZXoKnULUx4=",
    version = "v0.2.0",
)

go_repository(
    name = "com_github_prometheus_tsdb",
    importpath = "github.com/prometheus/tsdb",
    sum = "h1:YZcsG11NqnK4czYLrWd9mpEuAJIHVQLwdrleYfszMAA=",
    version = "v0.7.1",
)

go_repository(
    name = "com_github_puerkitobio_purell",
    importpath = "github.com/PuerkitoBio/purell",
    sum = "h1:WEQqlqaGbrPkxLJWfBwQmfEAE1Z7ONdDLqrN38tNFfI=",
    version = "v1.1.1",
)

go_repository(
    name = "com_github_puerkitobio_urlesc",
    importpath = "github.com/PuerkitoBio/urlesc",
    sum = "h1:d+Bc7a5rLufV/sSk/8dngufqelfh6jnri85riMAaF/M=",
    version = "v0.0.0-20170810143723-de5bf2ad4578",
)

go_repository(
    name = "com_github_quasilyte_go_consistent",
    importpath = "github.com/quasilyte/go-consistent",
    sum = "h1:JoUA0uz9U0FVFq5p4LjEq4C0VgQ0El320s3Ms0V4eww=",
    version = "v0.0.0-20190521200055-c6f3937de18c",
)

go_repository(
    name = "com_github_quasilyte_go_ruleguard",
    importpath = "github.com/quasilyte/go-ruleguard",
    sum = "h1:DvnesvLtRPQOvaUbfXfh0tpMHg29by0H7F2U+QIkSu8=",
    version = "v0.1.2-0.20200318202121-b00d7a75d3d8",
)

go_repository(
    name = "com_github_remyoudompheng_bigfft",
    importpath = "github.com/remyoudompheng/bigfft",
    sum = "h1:/NRJ5vAYoqz+7sG51ubIDHXeWO8DlTSrToPu6q11ziA=",
    version = "v0.0.0-20170806203942-52369c62f446",
)

go_repository(
    name = "com_github_rogpeppe_fastuuid",
    importpath = "github.com/rogpeppe/fastuuid",
    sum = "h1:Ppwyp6VYCF1nvBTXL3trRso7mXMlRrw9ooo375wvi2s=",
    version = "v1.2.0",
)

go_repository(
    name = "com_github_rubiojr_go_vhd",
    importpath = "github.com/rubiojr/go-vhd",
    sum = "h1:ht7N4d/B7Ezf58nvMNVF3OlvDlz9pp+WHVcRNS0nink=",
    version = "v0.0.0-20160810183302-0bfd3b39853c",
)

go_repository(
    name = "com_github_russross_blackfriday",
    importpath = "github.com/russross/blackfriday",
    sum = "h1:HyvC0ARfnZBqnXwABFeSZHpKvJHJJfPz81GNueLj0oo=",
    version = "v1.5.2",
)

go_repository(
    name = "com_github_russross_blackfriday_v2",
    importpath = "github.com/russross/blackfriday/v2",
    sum = "h1:JIOH55/0cWyOuilr9/qlrm0BSXldqnqwMsf35Ld67mk=",
    version = "v2.1.0",
)

go_repository(
    name = "com_github_ryancurrah_gomodguard",
    importpath = "github.com/ryancurrah/gomodguard",
    sum = "h1:DWbye9KyMgytn8uYpuHkwf0RHqAYO6Ay/D0TbCpPtVU=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_ryanuber_columnize",
    importpath = "github.com/ryanuber/columnize",
    sum = "h1:UFr9zpz4xgTnIE5yIMtWAMngCdZ9p/+q6lTbgelo80M=",
    version = "v0.0.0-20160712163229-9b3edd62028f",
)

go_repository(
    name = "com_github_sassoftware_go_rpmutils",
    importpath = "github.com/sassoftware/go-rpmutils",
    sum = "h1:+gCnWOZV8Z/8jehJ2CdqB47Z3S+SREmQcuXkRFLNsiI=",
    version = "v0.0.0-20190420191620-a8f1baeba37b",
)

go_repository(
    name = "com_github_sclevine_spec",
    importpath = "github.com/sclevine/spec",
    sum = "h1:1Jwdf9jSfDl9NVmt8ndHqbTZ7XCCPbh1jI3hkDBHVYA=",
    version = "v1.2.0",
)

go_repository(
    name = "com_github_sean_seed",
    importpath = "github.com/sean-/seed",
    sum = "h1:nn5Wsu0esKSJiIVhscUtVbo7ada43DJhG55ua/hjS5I=",
    version = "v0.0.0-20170313163322-e2103e2c3529",
)

go_repository(
    name = "com_github_securego_gosec",
    importpath = "github.com/securego/gosec",
    sum = "h1:rq2/kILQnPtq5oL4+IAjgVOjh5e2yj2aaCYi7squEvI=",
    version = "v0.0.0-20200401082031-e946c8c39989",
)

go_repository(
    name = "com_github_securego_gosec_v2",
    importpath = "github.com/securego/gosec/v2",
    sum = "h1:y/9mCF2WPDbSDpL3QDWZD3HHGrSYw0QSHnCqTfs4JPE=",
    version = "v2.3.0",
)

go_repository(
    name = "com_github_sergi_go_diff",
    importpath = "github.com/sergi/go-diff",
    sum = "h1:Kpca3qRNrduNnOQeazBd0ysaKrUJiIuISHxogkT9RPQ=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_shirou_gopsutil",
    importpath = "github.com/shirou/gopsutil",
    sum = "h1:WokF3GuxBeL+n4Lk4Fa8v9mbdjlrl7bHuneF4N1bk2I=",
    version = "v0.0.0-20190901111213-e4ec7b275ada",
)

go_repository(
    name = "com_github_shirou_w32",
    importpath = "github.com/shirou/w32",
    sum = "h1:udFKJ0aHUL60LboW/A+DfgoHVedieIzIXE8uylPue0U=",
    version = "v0.0.0-20160930032740-bb4de0191aa4",
)

go_repository(
    name = "com_github_shurcool_go",
    importpath = "github.com/shurcooL/go",
    sum = "h1:MZM7FHLqUHYI0Y/mQAt3d2aYa0SiNms/hFqC9qJYolM=",
    version = "v0.0.0-20180423040247-9e1955d9fb6e",
)

go_repository(
    name = "com_github_shurcool_go_goon",
    importpath = "github.com/shurcooL/go-goon",
    sum = "h1:llrF3Fs4018ePo4+G/HV/uQUqEI1HMDjCeOf2V6puPc=",
    version = "v0.0.0-20170922171312-37c2f522c041",
)

go_repository(
    name = "com_github_shurcool_sanitized_anchor_name",
    importpath = "github.com/shurcooL/sanitized_anchor_name",
    sum = "h1:PdmoCO6wvbs+7yrJyMORt4/BmY5IYyJwS/kOiWx8mHo=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_smartystreets_go_aws_auth",
    importpath = "github.com/smartystreets/go-aws-auth",
    sum = "h1:hp2CYQUINdZMHdvTdXtPOY2ainKl4IoMcpAXEf2xj3Q=",
    version = "v0.0.0-20180515143844-0c1422d1fdb9",
)

go_repository(
    name = "com_github_smartystreets_gunit",
    importpath = "github.com/smartystreets/gunit",
    sum = "h1:RyPDUFcJbvtXlhJPk7v+wnxZRY2EUokhEYl2EJOPToI=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_soheilhy_cmux",
    importpath = "github.com/soheilhy/cmux",
    sum = "h1:0HKaf1o97UwFjHH9o5XsHUOF+tqmdA7KEzXLpiyaw0E=",
    version = "v0.1.4",
)

go_repository(
    name = "com_github_sourcegraph_go_diff",
    importpath = "github.com/sourcegraph/go-diff",
    sum = "h1:lhIKJ2nXLZZ+AfbHpYxTn0pXpNTTui0DX7DO3xeb1Zs=",
    version = "v0.5.3",
)

go_repository(
    name = "com_github_spf13_afero",
    importpath = "github.com/spf13/afero",
    sum = "h1:5jhuqJyZCZf2JRofRvN/nIFgIWNzPa3/Vz8mYylgbWc=",
    version = "v1.2.2",
)

go_repository(
    name = "com_github_spf13_cast",
    importpath = "github.com/spf13/cast",
    sum = "h1:oget//CVOEoFewqQxwr0Ej5yjygnqGkvggSE/gB35Q8=",
    version = "v1.3.0",
)

go_repository(
    name = "com_github_spf13_cobra",
    importpath = "github.com/spf13/cobra",
    sum = "h1:X+jTBEBqF0bHN+9cSMgmfuvv2VHJ9ezmFNf9Y/XstYU=",
    version = "v1.5.0",
)

go_repository(
    name = "com_github_spf13_jwalterweatherman",
    importpath = "github.com/spf13/jwalterweatherman",
    sum = "h1:XHEdyB+EcvlqZamSM4ZOMGlc93t6AcsBEu9Gc1vn7yk=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_spf13_pflag",
    importpath = "github.com/spf13/pflag",
    sum = "h1:iy+VFUOCP1a+8yFto/drg2CJ5u0yRoB7fZw3DKv/JXA=",
    version = "v1.0.5",
)

go_repository(
    name = "com_github_spf13_viper",
    importpath = "github.com/spf13/viper",
    sum = "h1:yXHLWeravcrgGyFSyCgdYpXQ9dR9c/WED3pg1RhxqEU=",
    version = "v1.4.0",
)

go_repository(
    name = "com_github_stackexchange_wmi",
    importpath = "github.com/StackExchange/wmi",
    sum = "h1:G0m3OIz70MZUWq3EgK3CesDbo8upS2Vm9/P3FtgI+Jk=",
    version = "v0.0.0-20190523213315-cbe66965904d",
)

go_repository(
    name = "com_github_subosito_gotenv",
    importpath = "github.com/subosito/gotenv",
    sum = "h1:Slr1R9HxAlEKefgq5jn9U+DnETlIUa6HfgEzj0g5d7s=",
    version = "v1.2.0",
)

go_repository(
    name = "com_github_tdakkota_asciicheck",
    importpath = "github.com/tdakkota/asciicheck",
    sum = "h1:HxLVTlqcHhFAz3nWUcuvpH7WuOMv8LQoCWmruLfFH2U=",
    version = "v0.0.0-20200416200610-e657995f937b",
)

go_repository(
    name = "com_github_tetafro_godot",
    importpath = "github.com/tetafro/godot",
    sum = "h1:Dib7un+rYJFUi8vN0Bk6EHheKy6fv6ZzFURHw75g6m8=",
    version = "v0.4.2",
)

go_repository(
    name = "com_github_timakin_bodyclose",
    importpath = "github.com/timakin/bodyclose",
    sum = "h1:ig99OeTyDwQWhPe2iw9lwfQVF1KB3Q4fpP3X7/2VBG8=",
    version = "v0.0.0-20200424151742-cb6215831a94",
)

go_repository(
    name = "com_github_tj_assert",
    importpath = "github.com/tj/assert",
    sum = "h1:Rw8kxzWo1mr6FSaYXjQELRe88y2KdfynXdnK72rdjtA=",
    version = "v0.0.0-20171129193455-018094318fb0",
)

go_repository(
    name = "com_github_tj_go_elastic",
    importpath = "github.com/tj/go-elastic",
    sum = "h1:eGaGNxrtoZf/mBURsnNQKDR7u50Klgcf2eFDQEnc8Bc=",
    version = "v0.0.0-20171221160941-36157cbbebc2",
)

go_repository(
    name = "com_github_tj_go_kinesis",
    importpath = "github.com/tj/go-kinesis",
    sum = "h1:m74UWYy+HBs+jMFR9mdZU6shPewugMyH5+GV6LNgW8w=",
    version = "v0.0.0-20171128231115-08b17f58cb1b",
)

go_repository(
    name = "com_github_tj_go_spin",
    importpath = "github.com/tj/go-spin",
    sum = "h1:lhdWZsvImxvZ3q1C5OIB7d72DuOwP4O2NdBg9PyzNds=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_tmc_grpc_websocket_proxy",
    importpath = "github.com/tmc/grpc-websocket-proxy",
    sum = "h1:LnC5Kc/wtumK+WB441p7ynQJzVuNRJiqddSIE3IlSEQ=",
    version = "v0.0.0-20190109142713-0ad062ec5ee5",
)

go_repository(
    name = "com_github_tommy_muehle_go_mnd",
    importpath = "github.com/tommy-muehle/go-mnd",
    sum = "h1:RC4maTWLKKwb7p1cnoygsbKIgNlJqSYBeAFON3Ar8As=",
    version = "v1.3.1-0.20200224220436-e6f9a994e8fa",
)

go_repository(
    name = "com_github_ugorji_go",
    importpath = "github.com/ugorji/go",
    sum = "h1:j4s+tAvLfL3bZyefP2SEWmhBzmuIlH/eqNuPdFPgngw=",
    version = "v1.1.4",
)

go_repository(
    name = "com_github_ugorji_go_codec",
    importpath = "github.com/ugorji/go/codec",
    sum = "h1:3SVOIvH7Ae1KRYyQWRjXWJEA9sS/c/pjvH++55Gr648=",
    version = "v0.0.0-20181204163529-d75b2dcb6bc8",
)

go_repository(
    name = "com_github_ulikunitz_xz",
    importpath = "github.com/ulikunitz/xz",
    sum = "h1:YvTNdFzX6+W5m9msiYg/zpkSURPPtOlzbqYjrFn7Yt4=",
    version = "v0.5.7",
)

go_repository(
    name = "com_github_ultraware_funlen",
    importpath = "github.com/ultraware/funlen",
    sum = "h1:Av96YVBwwNSe4MLR7iI/BIa3VyI7/djnto/pK3Uxbdo=",
    version = "v0.0.2",
)

go_repository(
    name = "com_github_ultraware_whitespace",
    importpath = "github.com/ultraware/whitespace",
    sum = "h1:If7Va4cM03mpgrNH9k49/VOicWpGoG70XPBFFODYDsg=",
    version = "v0.0.4",
)

go_repository(
    name = "com_github_urfave_cli",
    importpath = "github.com/urfave/cli",
    sum = "h1:+mkCCcOFKPnCmVYVcURKps1Xe+3zP90gSYGNfRkjoIY=",
    version = "v1.22.1",
)

go_repository(
    name = "com_github_uudashr_gocognit",
    importpath = "github.com/uudashr/gocognit",
    sum = "h1:MoG2fZ0b/Eo7NXoIwCVFLG5JED3qgQz5/NEE+rOsjPs=",
    version = "v1.0.1",
)

go_repository(
    name = "com_github_valyala_bytebufferpool",
    importpath = "github.com/valyala/bytebufferpool",
    sum = "h1:GqA5TC/0021Y/b9FG4Oi9Mr3q7XYx6KllzawFIhcdPw=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_valyala_fasthttp",
    importpath = "github.com/valyala/fasthttp",
    sum = "h1:dzZJf2IuMiclVjdw0kkT+f9u4YdrapbNyGAN47E/qnk=",
    version = "v1.2.0",
)

go_repository(
    name = "com_github_valyala_quicktemplate",
    importpath = "github.com/valyala/quicktemplate",
    sum = "h1:BaO1nHTkspYzmAjPXj0QiDJxai96tlcZyKcI9dyEGvM=",
    version = "v1.2.0",
)

go_repository(
    name = "com_github_valyala_tcplisten",
    importpath = "github.com/valyala/tcplisten",
    sum = "h1:0R4NLDRDZX6JcmhJgXi5E4b8Wg84ihbmUKp/GvSPEzc=",
    version = "v0.0.0-20161114210144-ceec8f93295a",
)

go_repository(
    name = "com_github_vdemeester_k8s_pkg_credentialprovider",
    importpath = "github.com/vdemeester/k8s-pkg-credentialprovider",
    sum = "h1:lWvSzFrGhtYgApDvR5X+43rqfpLzRumLzypyL1YhDww=",
    version = "v1.18.1-0.20201019120933-f1d16962a4db",
)

go_repository(
    name = "com_github_vmware_govmomi",
    importpath = "github.com/vmware/govmomi",
    sum = "h1:gpw/0Ku+6RgF3jsi7fnCLmlcikBHfKBCUcu1qgc16OU=",
    version = "v0.20.3",
)

go_repository(
    name = "com_github_xanzy_go_gitlab",
    importpath = "github.com/xanzy/go-gitlab",
    sum = "h1:tBm+OXv1t+KBsqlXkSDFz+YUjRM0GFsjpOWYOod3Ebs=",
    version = "v0.32.0",
)

go_repository(
    name = "com_github_xi2_xz",
    importpath = "github.com/xi2/xz",
    sum = "h1:nIPpBwaJSVYIxUFsDv3M8ofmx9yWTog9BfvIu0q41lo=",
    version = "v0.0.0-20171230120015-48954b6210f8",
)

go_repository(
    name = "com_github_xiang90_probing",
    importpath = "github.com/xiang90/probing",
    sum = "h1:eY9dn8+vbi4tKz5Qo6v2eYzo7kUS51QINcR5jNpbZS8=",
    version = "v0.0.0-20190116061207-43a291ad63a2",
)

go_repository(
    name = "com_github_xordataexchange_crypt",
    importpath = "github.com/xordataexchange/crypt",
    sum = "h1:ESFSdwYZvkeru3RtdrYueztKhOBCSAAzS4Gf+k0tEow=",
    version = "v0.0.3-0.20170626215501-b2862e3d0a77",
)

go_repository(
    name = "com_google_cloud_go_firestore",
    importpath = "cloud.google.com/go/firestore",
    sum = "h1:9x7Bx0A9R5/M9jibeJeZWqjeVEIxYW9fZYqB9a70/bY=",
    version = "v1.1.0",
)

go_repository(
    name = "com_sourcegraph_sqs_pbtypes",
    importpath = "sourcegraph.com/sqs/pbtypes",
    sum = "h1:f7lAwqviDEGvON4kRv0o5V7FT/IQK+tbkF664XMbP3o=",
    version = "v1.0.0",
)

go_repository(
    name = "dev_gocloud",
    importpath = "gocloud.dev",
    sum = "h1:EDRyaRAnMGSq/QBto486gWFxMLczAfIYUmusV7XLNBM=",
    version = "v0.19.0",
)

go_repository(
    name = "in_gopkg_cheggaaa_pb_v1",
    importpath = "gopkg.in/cheggaaa/pb.v1",
    sum = "h1:Ev7yu1/f6+d+b3pi5vPdRPc6nNtP1umSfcWiEfRqv6I=",
    version = "v1.0.25",
)

go_repository(
    name = "in_gopkg_gcfg_v1",
    importpath = "gopkg.in/gcfg.v1",
    sum = "h1:m8OOJ4ccYHnx2f4gQwpno8nAX5OGOh7RLaaz0pj3Ogs=",
    version = "v1.2.3",
)

go_repository(
    name = "in_gopkg_inf_v0",
    importpath = "gopkg.in/inf.v0",
    sum = "h1:73M5CoZyi3ZLMOyDlQh031Cx6N9NDJ2Vvfl76EDAgDc=",
    version = "v0.9.1",
)

go_repository(
    name = "in_gopkg_natefinch_lumberjack_v2",
    importpath = "gopkg.in/natefinch/lumberjack.v2",
    sum = "h1:1Lc07Kr7qY4U2YPouBjpCLxpiyxIVoxqXgkXLknAOE8=",
    version = "v2.0.0",
)

go_repository(
    name = "in_gopkg_resty_v1",
    importpath = "gopkg.in/resty.v1",
    sum = "h1:CuXP0Pjfw9rOuY6EP+UvtNvt5DSqHpIxILZKT/quCZI=",
    version = "v1.12.0",
)

go_repository(
    name = "in_gopkg_square_go_jose_v2",
    importpath = "gopkg.in/square/go-jose.v2",
    sum = "h1:orlkJ3myw8CN1nVQHBFfloD+L3egixIa4FvUP6RosSA=",
    version = "v2.2.2",
)

go_repository(
    name = "in_gopkg_warnings_v0",
    importpath = "gopkg.in/warnings.v0",
    sum = "h1:wFXVbFY8DY5/xOe1ECiWdKCzZlxgshcYVNkBHstARME=",
    version = "v0.1.2",
)

go_repository(
    name = "io_etcd_go_etcd",
    importpath = "go.etcd.io/etcd",
    sum = "h1:VcrIfasaLFkyjk6KNlXQSzO+B0fZcnECiDrKJsfxka0=",
    version = "v0.0.0-20191023171146-3cf2f69b5738",
)

go_repository(
    name = "io_gitea_code_sdk_gitea",
    importpath = "code.gitea.io/sdk/gitea",
    sum = "h1:hvDCz4wtFvo7rf5Ebj8tGd4aJ4wLPKX3BKFX9Dk1Pgs=",
    version = "v0.12.0",
)

go_repository(
    name = "io_k8s_api",
    build_file_proto_mode = "disable_global",
    importpath = "k8s.io/api",
    strip_prefix = "api-0.20.1",
    type = "zip",
    urls = ["https://github.com/kubernetes/api/archive/refs/tags/v0.20.1.zip"],
)

go_repository(
    name = "io_k8s_apimachinery",
    build_file_proto_mode = "disable_global",
    importpath = "k8s.io/apimachinery",
    strip_prefix = "apimachinery-0.20.1",
    type = "zip",
    urls = ["https://github.com/kubernetes/apimachinery/archive/refs/tags/v0.20.1.zip"],
)

go_repository(
    name = "io_k8s_apiserver",
    build_file_proto_mode = "disable_global",
    importpath = "k8s.io/apiserver",
    strip_prefix = "apiserver-0.18.8",
    type = "zip",
    urls = ["https://github.com/kubernetes/apiserver/archive/refs/tags/v0.18.8.zip"],
)

go_repository(
    name = "io_k8s_client_go",
    build_file_proto_mode = "disable_global",
    importpath = "k8s.io/client-go",
    strip_prefix = "client-go-0.18.8",
    type = "zip",
    urls = ["https://github.com/kubernetes/client-go/archive/refs/tags/v0.18.8.zip"],
)

go_repository(
    name = "io_k8s_cloud_provider",
    build_file_proto_mode = "disable_global",
    importpath = "k8s.io/cloud-provider",
    strip_prefix = "cloud-provider-0.18.8",
    type = "zip",
    urls = ["https://github.com/kubernetes/cloud-provider/archive/refs/tags/v0.18.8.zip"],
)

go_repository(
    name = "io_k8s_code_generator",
    build_file_proto_mode = "disable_global",
    importpath = "k8s.io/code-generator",
    strip_prefix = "code-generator-0.17.2",
    type = "zip",
    urls = ["https://github.com/kubernetes/code-generator/archive/refs/tags/v0.17.2.zip"],
)

go_repository(
    name = "io_k8s_component_base",
    build_file_proto_mode = "disable_global",
    importpath = "k8s.io/component-base",
    strip_prefix = "component-base-0.18.8",
    type = "zip",
    urls = ["https://github.com/kubernetes/component-base/archive/refs/tags/v0.18.8.zip"],
)

go_repository(
    name = "io_k8s_csi_translation_lib",
    build_file_proto_mode = "disable_global",
    importpath = "k8s.io/csi-translation-lib",
    strip_prefix = "csi-translation-lib-0.18.8",
    type = "zip",
    urls = ["https://github.com/kubernetes/csi-translation-lib/archive/refs/tags/v0.18.8.zip"],
)

go_repository(
    name = "io_k8s_gengo",
    build_file_proto_mode = "disable_global",
    importpath = "k8s.io/gengo",
    strip_prefix = "gengo-3a45101e95ac3a2015e6bf3e41269d624610c946",
    type = "zip",
    urls = ["https://github.com/kubernetes/gengo/archive/3a45101e95ac3a2015e6bf3e41269d624610c946.zip"],
)

go_repository(
    name = "io_k8s_klog",
    build_file_proto_mode = "disable_global",
    importpath = "k8s.io/klog",
    strip_prefix = "klog-1.0.0",
    type = "zip",
    urls = ["https://github.com/kubernetes/klog/archive/refs/tags/v1.0.0.zip"],
)

go_repository(
    name = "io_k8s_kube_openapi",
    build_file_proto_mode = "disable_global",
    importpath = "k8s.io/kube-openapi",
    strip_prefix = "kube-openapi-d219536bb9fd63731fa65ca4b42f14bc2f10c61b",
    type = "zip",
    urls = ["https://github.com/kubernetes/kube-openapi/archive/d219536bb9fd63731fa65ca4b42f14bc2f10c61b.zip"],
)

go_repository(
    name = "io_k8s_legacy_cloud_providers",
    build_file_proto_mode = "disable_global",
    importpath = "k8s.io/legacy-cloud-providers",
    strip_prefix = "legacy-cloud-providers-0.18.8",
    type = "zip",
    urls = ["https://github.com/kubernetes/legacy-cloud-providers/archive/refs/tags/v0.18.8.zip"],
)

go_repository(
    name = "io_k8s_sigs_structured_merge_diff",
    build_file_proto_mode = "disable_global",
    importpath = "sigs.k8s.io/structured-merge-diff",
    sum = "h1:zD2IemQ4LmOcAumeiyDWXKUI2SO0NYDe3H6QGvPOVgU=",
    version = "v1.0.1-0.20191108220359-b1b620dd3f06",
)

go_repository(
    name = "io_k8s_sigs_yaml",
    build_file_proto_mode = "disable_global",
    importpath = "sigs.k8s.io/yaml",
    sum = "h1:kr/MCeFWJWTwyaHoR9c8EjH9OumOmoF9YGiZd7lFm/Q=",
    version = "v1.2.0",
)

go_repository(
    name = "io_k8s_utils",
    build_file_proto_mode = "disable_global",
    importpath = "k8s.io/utils",
    sum = "h1:d4vVOjXm687F1iLSP2q3lyPPuyvTUt3aVoBpi2DqRsU=",
    version = "v0.0.0-20200324210504-a9aa75ae1b89",
)

go_repository(
    name = "io_opencensus_go_contrib_exporter_aws",
    importpath = "contrib.go.opencensus.io/exporter/aws",
    sum = "h1:YsbWYxDZkC7x2OxlsDEYvvEXZ3cBI3qBgUK5BqkZvRw=",
    version = "v0.0.0-20181029163544-2befc13012d0",
)

go_repository(
    name = "io_opencensus_go_contrib_exporter_ocagent",
    importpath = "contrib.go.opencensus.io/exporter/ocagent",
    sum = "h1:TKXjQSRS0/cCDrP7KvkgU6SmILtF/yV2TOs/02K/WZQ=",
    version = "v0.5.0",
)

go_repository(
    name = "io_opencensus_go_contrib_exporter_stackdriver",
    importpath = "contrib.go.opencensus.io/exporter/stackdriver",
    sum = "h1:Dll2uFfOVI3fa8UzsHyP6z0M6fEc9ZTAMo+Y3z282Xg=",
    version = "v0.12.1",
)

go_repository(
    name = "io_opencensus_go_contrib_integrations_ocsql",
    importpath = "contrib.go.opencensus.io/integrations/ocsql",
    sum = "h1:kfg5Yyy1nYUrqzyfW5XX+dzMASky8IJXhtHe0KTYNS4=",
    version = "v0.1.4",
)

go_repository(
    name = "io_opencensus_go_contrib_resource",
    importpath = "contrib.go.opencensus.io/resource",
    sum = "h1:4r2CANuYhKGmYWP02+5E94rLRcS/YeD+KlxSrOsMxk0=",
    version = "v0.1.1",
)

go_repository(
    name = "org_bazil_fuse",
    importpath = "bazil.org/fuse",
    sum = "h1:FNCRpXiquG1aoyqcIWVFmpTSKVcx2bQD38uZZeGtdlw=",
    version = "v0.0.0-20180421153158-65cc252bf669",
)

go_repository(
    name = "org_gonum_v1_gonum",
    importpath = "gonum.org/v1/gonum",
    sum = "h1:OB/uP/Puiu5vS5QMRPrXCDWUPb+kt8f1KW8oQzFejQw=",
    version = "v0.0.0-20190331200053-3d26580ed485",
)

go_repository(
    name = "org_gonum_v1_netlib",
    importpath = "gonum.org/v1/netlib",
    sum = "h1:jRyg0XfpwWlhEV8mDfdNGBeSJM2fuyh9Yjrnd8kF2Ts=",
    version = "v0.0.0-20190331212654-76723241ea4e",
)

go_repository(
    name = "org_modernc_cc",
    importpath = "modernc.org/cc",
    sum = "h1:nPibNuDEx6tvYrUAtvDTTw98rx5juGsa5zuDnKwEEQQ=",
    version = "v1.0.0",
)

go_repository(
    name = "org_modernc_golex",
    importpath = "modernc.org/golex",
    sum = "h1:wWpDlbK8ejRfSyi0frMyhilD3JBvtcx2AdGDnU+JtsE=",
    version = "v1.0.0",
)

go_repository(
    name = "org_modernc_mathutil",
    importpath = "modernc.org/mathutil",
    sum = "h1:93vKjrJopTPrtTNpZ8XIovER7iCIH1QU7wNbOQXC60I=",
    version = "v1.0.0",
)

go_repository(
    name = "org_modernc_strutil",
    importpath = "modernc.org/strutil",
    sum = "h1:XVFtQwFVwc02Wk+0L/Z/zDDXO81r5Lhe6iMKmGX3KhE=",
    version = "v1.0.0",
)

go_repository(
    name = "org_modernc_xc",
    importpath = "modernc.org/xc",
    sum = "h1:7ccXrupWZIS3twbUGrtKmHS2DXY6xegFua+6O3xgAFU=",
    version = "v1.0.0",
)

go_repository(
    name = "tools_gotest",
    importpath = "gotest.tools",
    sum = "h1:VsBPFP1AI068pPrMxtb/S8Zkgf9xEmTLJjfM+P5UIEo=",
    version = "v2.2.0+incompatible",
)

go_repository(
    name = "com_github_cespare_xxhash_v2",
    importpath = "github.com/cespare/xxhash/v2",
    sum = "h1:YRXhKfTDauu4ajMg1TPgFO5jnlC2HCbmLXMcTG5cbYE=",
    version = "v2.1.2",
)

go_repository(
    name = "com_github_dgryski_go_rendezvous",
    importpath = "github.com/dgryski/go-rendezvous",
    sum = "h1:lO4WD4F/rVNCu3HqELle0jiPLLBs70cWOduZpkS1E78=",
    version = "v0.0.0-20200823014737-9f7001d12a5f",
)

go_repository(
    name = "com_github_go_redis_redis_v7",
    importpath = "github.com/go-redis/redis/v7",
    sum = "h1:7obg6wUoj05T0EpY0o8B59S9w5yeMWql7sw2kwNW1x4=",
    version = "v7.4.0",
)

go_repository(
    name = "com_github_go_redis_redis_v8",
    importpath = "github.com/go-redis/redis/v8",
    sum = "h1:AcZZR7igkdvfVmQTPnu9WE37LRrO/YrBH5zWyjDC0oI=",
    version = "v8.11.5",
)

go_repository(
    name = "com_github_jmespath_go_jmespath_internal_testify",
    importpath = "github.com/jmespath/go-jmespath/internal/testify",
    sum = "h1:shLQSRRSCCPj3f2gpwzGwWFoC7ycTf1rcQZHOlsJ6N8=",
    version = "v1.5.1",
)

go_repository(
    name = "com_github_nxadm_tail",
    importpath = "github.com/nxadm/tail",
    sum = "h1:nPr65rt6Y5JFSKQO7qToXr7pePgD6Gwiw05lkbyAQTE=",
    version = "v1.4.8",
)

go_repository(
    name = "in_gopkg_yaml_v3",
    importpath = "gopkg.in/yaml.v3",
    sum = "h1:h8qDotaEPuJATrMmW04NCwg7v22aHH28wwpauUhK9Oo=",
    version = "v3.0.0-20210107192922-496545a6307b",
)

go_repository(
    name = "io_opentelemetry_go_otel",
    importpath = "go.opentelemetry.io/otel",
    sum = "h1:Lenfy7QHRXPZVsw/12CWpxX6d/JkrX8wrx2vO8G80Ng=",
    version = "v0.19.0",
)

go_repository(
    name = "com_github_alicebob_gopher_json",
    importpath = "github.com/alicebob/gopher-json",
    sum = "h1:HbKu58rmZpUGpz5+4FfNmIU+FmZg2P3Xaj2v2bfNWmk=",
    version = "v0.0.0-20200520072559-a9ecdc9d1d3a",
)

go_repository(
    name = "com_github_alicebob_miniredis_v2",
    importpath = "github.com/alicebob/miniredis/v2",
    sum = "h1:kohgdtN58KW/r9ZDVmMJE3MrfbumwsDQStd0LPAGmmw=",
    version = "v2.13.3",
)

go_repository(
    name = "com_github_yuin_gopher_lua",
    importpath = "github.com/yuin/gopher-lua",
    sum = "h1:ZkM6LRnq40pR1Ox0hTHlnpkcOTuFIDQpZ1IN8rKKhX0=",
    version = "v0.0.0-20191220021717-ab39c6098bdb",
)

go_repository(
    name = "com_github_elliotchance_redismock",
    importpath = "github.com/elliotchance/redismock",
    sum = "h1:Lgi2CLfVB3PamPI1SPqjJf5AiGisPFMWvIOCiRIq+sI=",
    version = "v1.5.3",
)

go_repository(
    name = "com_github_go_redis_redis",
    importpath = "github.com/go-redis/redis",
    sum = "h1:K0pv1D7EQUjfyoMql+r/jZqCLizCGKFlFgcHWWmHQjg=",
    version = "v6.15.9+incompatible",
)

go_repository(
    name = "com_github_alicebob_miniredis",
    importpath = "github.com/alicebob/miniredis",
    sum = "h1:yBHoLpsyjupjz3NL3MhKMVkR41j82Yjf3KFv7ApYzUI=",
    version = "v2.5.0+incompatible",
)

go_repository(
    name = "com_github_elliotchance_redismock_v7",
    importpath = "github.com/elliotchance/redismock/v7",
    sum = "h1:yLNTNVcL2hST92Epjtp4UmamX0TzWrXXe9j5J/UqNgA=",
    version = "v7.0.1",
)

go_repository(
    name = "com_github_gomodule_redigo",
    importpath = "github.com/gomodule/redigo",
    sum = "h1:K/R+8tc58AaqLkqG2Ol3Qk+DR/TlNuhuh457pBFPtt0=",
    version = "v2.0.0+incompatible",
)

go_repository(
    name = "org_uber_go_goleak",
    importpath = "go.uber.org/goleak",
    sum = "h1:z+mqJhf6ss6BSfSM671tgKyZBFPTTJM+HLxnhPC3wu0=",
    version = "v1.1.10",
)

go_repository(
    name = "com_github_dchest_authcookie",
    importpath = "github.com/dchest/authcookie",
    sum = "h1:xizeG5ksKSdyNaom2//2Bow4hLWqXkCql36nrL9iEUI=",
    version = "v0.0.0-20190824115100-f900d2294c8e",
)

go_repository(
    name = "cc_mvdan_editorconfig",
    importpath = "mvdan.cc/editorconfig",
    sum = "h1:VBYz8greWWP8BDpRX0v7SDv/8rNlZVmdHdO4ZSsHj/E=",
    version = "v0.1.1-0.20200121172147-e40951bde157",
)

go_repository(
    name = "cc_mvdan_sh_v3",
    importpath = "mvdan.cc/sh/v3",
    sum = "h1:+fZaWcXWRjYAvqzEKoDhDM3DkxdDUykU2iw0VMKFe9s=",
    version = "v3.2.4",
)

go_repository(
    name = "com_github_pkg_diff",
    importpath = "github.com/pkg/diff",
    sum = "h1:+/+DxvQaYifJ+grD4klzrS5y+KJXldn/2YTl5JG+vZ8=",
    version = "v0.0.0-20200914180035-5b29258ca4f7",
)

go_repository(
    name = "org_golang_x_term",
    importpath = "golang.org/x/term",
    sum = "h1:JGgROgKl9N8DuW20oFS5gxc+lE67/N3FcwmBPMe7ArY=",
    version = "v0.0.0-20210927222741-03fcf44c2211",
)

go_repository(
    name = "com_github_cockroachdb_apd",
    importpath = "github.com/cockroachdb/apd",
    sum = "h1:3LFP3629v+1aKXU5Q37mxmRxX/pIu1nijXydLShEq5I=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_jackc_chunkreader",
    importpath = "github.com/jackc/chunkreader",
    sum = "h1:4s39bBR8ByfqH+DKm8rQA3E1LHZWB9XWcrz8fqaZbe0=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_jackc_chunkreader_v2",
    importpath = "github.com/jackc/chunkreader/v2",
    sum = "h1:i+RDz65UE+mmpjTfyz0MoVTnzeYxroil2G82ki7MGG8=",
    version = "v2.0.1",
)

go_repository(
    name = "com_github_jackc_pgconn",
    importpath = "github.com/jackc/pgconn",
    sum = "h1:FmjZ0rOyXTr1wfWs45i4a9vjnjWUAGpMuQLD9OSs+lw=",
    version = "v1.8.0",
)

go_repository(
    name = "com_github_jackc_pgio",
    importpath = "github.com/jackc/pgio",
    sum = "h1:g12B9UwVnzGhueNavwioyEEpAmqMe1E/BN9ES+8ovkE=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_jackc_pgmock",
    importpath = "github.com/jackc/pgmock",
    sum = "h1:JVX6jT/XfzNqIjye4717ITLaNwV9mWbJx0dLCpcRzdA=",
    version = "v0.0.0-20190831213851-13a1b77aafa2",
)

go_repository(
    name = "com_github_jackc_pgpassfile",
    importpath = "github.com/jackc/pgpassfile",
    sum = "h1:/6Hmqy13Ss2zCq62VdNG8tM1wchn8zjSGOBJ6icpsIM=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_jackc_pgproto3",
    importpath = "github.com/jackc/pgproto3",
    sum = "h1:FYYE4yRw+AgI8wXIinMlNjBbp/UitDJwfj5LqqewP1A=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_jackc_pgproto3_v2",
    importpath = "github.com/jackc/pgproto3/v2",
    sum = "h1:b1105ZGEMFe7aCvrT1Cca3VoVb4ZFMaFJLJcg/3zD+8=",
    version = "v2.0.6",
)

go_repository(
    name = "com_github_jackc_pgservicefile",
    importpath = "github.com/jackc/pgservicefile",
    sum = "h1:C8S2+VttkHFdOOCXJe+YGfa4vHYwlt4Zx+IVXQ97jYg=",
    version = "v0.0.0-20200714003250-2b9c44734f2b",
)

go_repository(
    name = "com_github_jackc_pgtype",
    importpath = "github.com/jackc/pgtype",
    sum = "h1:b3pDeuhbbzBYcg5kwNmNDun4pFUD/0AAr1kLXZLeNt8=",
    version = "v1.6.2",
)

go_repository(
    name = "com_github_jackc_pgx_v4",
    importpath = "github.com/jackc/pgx/v4",
    sum = "h1:xXTl+lSiF1eFQ4U7vUL493n/1q8ZhSDP962rSKhgRZo=",
    version = "v4.10.0",
)

go_repository(
    name = "com_github_jackc_puddle",
    importpath = "github.com/jackc/puddle",
    sum = "h1:JnPg/5Q9xVJGfjsO5CPUOjnJps1JaRUm8I9FXVCFK94=",
    version = "v1.1.3",
)

go_repository(
    name = "com_github_rs_xid",
    importpath = "github.com/rs/xid",
    sum = "h1:qd7wPTDkN6KQx2VmMBLrpHkiyQwgFXRnkOLacUiaSNY=",
    version = "v1.4.0",
)

go_repository(
    name = "com_github_rs_zerolog",
    importpath = "github.com/rs/zerolog",
    sum = "h1:MirSo27VyNi7RJYP3078AA1+Cyzd2GB66qy3aUHvsWY=",
    version = "v1.28.0",
)

go_repository(
    name = "com_github_shopspring_decimal",
    importpath = "github.com/shopspring/decimal",
    sum = "h1:jUIKcSPO9MoMJBbEoyE/RJoE8vz7Mb8AjvifMMwSyvY=",
    version = "v0.0.0-20200227202807-02e2044944cc",
)

go_repository(
    name = "com_github_zenazn_goji",
    importpath = "github.com/zenazn/goji",
    sum = "h1:RSQQAbXGArQ0dIDEq+PI6WqN6if+5KHu6x2Cx/GXLTQ=",
    version = "v0.9.0",
)

go_repository(
    name = "in_gopkg_inconshreveable_log15_v2",
    importpath = "gopkg.in/inconshreveable/log15.v2",
    sum = "h1:RlWgLqCMMIYYEVcAR5MDsuHlVkaIPDAF+5Dehzg8L5A=",
    version = "v2.0.0-20180818164646-67afb5ed74ec",
)

go_repository(
    name = "com_github_jackc_pgx",
    importpath = "github.com/jackc/pgx",
    sum = "h1:2zP5OD7kiyR3xzRYMhOcXVvkDZsImVXfj+yIyTQf3/o=",
    version = "v3.6.2+incompatible",
)

go_repository(
    name = "in_gopkg_guregu_null_v4",
    importpath = "gopkg.in/guregu/null.v4",
    sum = "h1:1Wm3S1WEA2I26Kq+6vcW+w0gcDo44YKYD7YIEJNHDjg=",
    version = "v4.0.0",
)

http_archive(
    name = "rules_rust",
    sha256 = "e6d835ee673f388aa5b62dc23d82db8fc76497e93fa47d8a4afe97abaf09b10d",
    strip_prefix = "rules_rust-f37b9d6a552e9412285e627f30cb124e709f4f7a",
    urls = [
        # Master branch as of 2021-01-27
        "https://harness-artifactory.harness.io/artifactory/rules-rust-github/archive/f37b9d6a552e9412285e627f30cb124e709f4f7a.tar.gz",
    ],
)

load("@rules_rust//rust:repositories.bzl", "rust_repositories")

go_repository(
    name = "com_github_jasonlvhit_gocron",
    importpath = "github.com/jasonlvhit/gocron",
    sum = "h1:qTt5qF3b3srDjeOIR4Le1LfeyvoYzJlYpqvG7tJX5YU=",
    version = "v0.0.1",
)

go_repository(
    name = "com_github_prashantv_gostub",
    importpath = "github.com/prashantv/gostub",
    sum = "h1:BTyx3RfQjRHnUWaGF9oQos79AlQ5k8WNktv7VGvVH4g=",
    version = "v1.1.0",
)

go_repository(
    name = "org_uber_go_automaxprocs",
    importpath = "go.uber.org/automaxprocs",
    sum = "h1:e1YG66Lrk73dn4qhg8WFSvhF0JuFQF0ERIp4rpuV8Qk=",
    version = "v1.5.1",
)

rust_repositories(
    edition = "2018",
    version = "1.49.0",
)

#load("//cargo:crates.bzl", "raze_fetch_remote_crates")
#
#raze_fetch_remote_crates()

go_repository(
    name = "com_github_afex_hystrix_go",
    importpath = "github.com/afex/hystrix-go",
    sum = "h1:rFw4nCn9iMW+Vajsk51NtYIcwSTkXr+JGrMd36kTDJw=",
    version = "v0.0.0-20180502004556-fa1af6a1f4f5",
)

go_repository(
    name = "com_github_apache_thrift",
    importpath = "github.com/apache/thrift",
    sum = "h1:5hryIiq9gtn+MiLVn0wP37kb/uTeRZgN08WoCsAhIhI=",
    version = "v0.13.0",
)

go_repository(
    name = "com_github_aryann_difflib",
    importpath = "github.com/aryann/difflib",
    sum = "h1:pv34s756C4pEXnjgPfGYgdhg/ZdajGhyOvzx8k+23nw=",
    version = "v0.0.0-20170710044230-e206f873d14a",
)

go_repository(
    name = "com_github_asaskevich_govalidator",
    importpath = "github.com/asaskevich/govalidator",
    sum = "h1:idn718Q4B6AGu/h5Sxe66HYVdqdGu2l9Iebqhi/AEoA=",
    version = "v0.0.0-20190424111038-f61b66f89f4a",
)

go_repository(
    name = "com_github_aws_aws_lambda_go",
    importpath = "github.com/aws/aws-lambda-go",
    sum = "h1:SuCy7H3NLyp+1Mrfp+m80jcbi9KYWAs9/BXwppwRDzY=",
    version = "v1.13.3",
)

go_repository(
    name = "com_github_aws_aws_sdk_go_v2",
    importpath = "github.com/aws/aws-sdk-go-v2",
    sum = "h1:qZ+woO4SamnH/eEbjM2IDLhRNwIwND/RQyVlBLp3Jqg=",
    version = "v0.18.0",
)

go_repository(
    name = "com_github_casbin_casbin_v2",
    importpath = "github.com/casbin/casbin/v2",
    sum = "h1:bTwon/ECRx9dwBy2ewRVr5OiqjeXSGiTUY74sDPQi/g=",
    version = "v2.1.2",
)

go_repository(
    name = "com_github_clbanning_x2j",
    importpath = "github.com/clbanning/x2j",
    sum = "h1:EdRZT3IeKQmfCSrgo8SZ8V3MEnskuJP0wCYNpe+aiXo=",
    version = "v0.0.0-20191024224557-825249438eec",
)

go_repository(
    name = "com_github_codahale_hdrhistogram",
    importpath = "github.com/codahale/hdrhistogram",
    sum = "h1:qMd81Ts1T2OTKmB4acZcyKaMtRnY5Y44NuXGX2GFJ1w=",
    version = "v0.0.0-20161010025455-3a0bb77429bd",
)

go_repository(
    name = "com_github_containerd_stargz_snapshotter_estargz",
    importpath = "github.com/containerd/stargz-snapshotter/estargz",
    sum = "h1:tnP4txDzNKsBOISNYG/f48Mt477CBeh9sS5rlu8MvSY=",
    version = "v0.0.0-20201217071531-2b97b583765b",
)

go_repository(
    name = "com_github_docopt_docopt_go",
    importpath = "github.com/docopt/docopt-go",
    sum = "h1:bWDMxwH3px2JBh6AyO7hdCn/PkvCZXii8TGj7sbtEbQ=",
    version = "v0.0.0-20180111231733-ee0de3bc6815",
)

go_repository(
    name = "com_github_eapache_go_resiliency",
    importpath = "github.com/eapache/go-resiliency",
    sum = "h1:1NtRmCAqadE2FN4ZcN6g90TP3uk8cg9rn9eNK2197aU=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_eapache_go_xerial_snappy",
    importpath = "github.com/eapache/go-xerial-snappy",
    sum = "h1:YEetp8/yCZMuEPMUDHG0CW/brkkEp8mzqk2+ODEitlw=",
    version = "v0.0.0-20180814174437-776d5712da21",
)

go_repository(
    name = "com_github_eapache_queue",
    importpath = "github.com/eapache/queue",
    sum = "h1:YOEu7KNc61ntiQlcEeUIoDTJ2o8mQznoNvUhiigpIqc=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_edsrzf_mmap_go",
    importpath = "github.com/edsrzf/mmap-go",
    sum = "h1:CEBF7HpRnUCSJgGUb5h1Gm7e3VkmVDrR8lvWVLtrOFw=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_form3tech_oss_jwt_go",
    importpath = "github.com/form3tech-oss/jwt-go",
    sum = "h1:TcekIExNqud5crz4xD2pavyTgWiPvpYe4Xau31I0PRk=",
    version = "v3.2.2+incompatible",
)

go_repository(
    name = "com_github_franela_goblin",
    importpath = "github.com/franela/goblin",
    sum = "h1:gb2Z18BhTPJPpLQWj4T+rfKHYCHxRHCtRxhKKjRidVw=",
    version = "v0.0.0-20200105215937-c9ffbefa60db",
)

go_repository(
    name = "com_github_franela_goreq",
    importpath = "github.com/franela/goreq",
    sum = "h1:a9ENSRDFBUPkJ5lCgVZh26+ZbGyoVJG7yb5SSzF5H54=",
    version = "v0.0.0-20171204163338-bcd34c9993f8",
)

go_repository(
    name = "com_github_gogo_googleapis",
    importpath = "github.com/gogo/googleapis",
    sum = "h1:kFkMAZBNAn4j7K0GiZr8cRYzejq68VbheufiV3YuyFI=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_golang_snappy",
    importpath = "github.com/golang/snappy",
    sum = "h1:fHPg5GQYlCeLIPB9BZqMVR5nR9A+IM5zcgeTdjMYmLA=",
    version = "v0.0.3",
)

go_repository(
    name = "com_github_gorilla_context",
    importpath = "github.com/gorilla/context",
    sum = "h1:AWwleXJkX/nhcU9bZSnZoi3h/qGYqQAGhq6zZe/aQW8=",
    version = "v1.1.1",
)

go_repository(
    name = "com_github_hudl_fargo",
    importpath = "github.com/hudl/fargo",
    sum = "h1:0U6+BtN6LhaYuTnIJq4Wyq5cpn6O2kWrxAtcqBmYY6w=",
    version = "v1.3.0",
)

go_repository(
    name = "com_github_influxdata_influxdb1_client",
    importpath = "github.com/influxdata/influxdb1-client",
    sum = "h1:/WZQPMZNsjZ7IlCpsLGdQBINg5bxKQ1K1sh6awxLtkA=",
    version = "v0.0.0-20191209144304-8bf82d3c094d",
)

go_repository(
    name = "com_github_jessevdk_go_flags",
    importpath = "github.com/jessevdk/go-flags",
    sum = "h1:4IU2WS7AumrZ/40jfhf4QVDMsQwqA7VEHozFRrGARJA=",
    version = "v1.4.0",
)

go_repository(
    name = "com_github_knetic_govaluate",
    importpath = "github.com/Knetic/govaluate",
    sum = "h1:1G1pk05UrOh0NlF1oeaaix1x8XzrfjIDK47TY0Zehcw=",
    version = "v3.0.1-0.20171022003610-9aa49832a739+incompatible",
)

go_repository(
    name = "com_github_lightstep_lightstep_tracer_common_golang_gogo",
    importpath = "github.com/lightstep/lightstep-tracer-common/golang/gogo",
    sum = "h1:143Bb8f8DuGWck/xpNUOckBVYfFbBTnLevfRZ1aVVqo=",
    version = "v0.0.0-20190605223551-bc2310a04743",
)

go_repository(
    name = "com_github_lightstep_lightstep_tracer_go",
    importpath = "github.com/lightstep/lightstep-tracer-go",
    sum = "h1:vi1F1IQ8N7hNWytK9DpJsUfQhGuNSc19z330K6vl4zk=",
    version = "v0.18.1",
)

go_repository(
    name = "com_github_lyft_protoc_gen_validate",
    importpath = "github.com/lyft/protoc-gen-validate",
    sum = "h1:KNt/RhmQTOLr7Aj8PsJ7mTronaFyx80mRTT9qF261dA=",
    version = "v0.0.13",
)

go_repository(
    name = "com_github_nats_io_jwt",
    importpath = "github.com/nats-io/jwt",
    sum = "h1:+RB5hMpXUUA2dfxuhBTEkMOrYmM+gKIZYS1KjSostMI=",
    version = "v0.3.2",
)

go_repository(
    name = "com_github_nats_io_nats_go",
    importpath = "github.com/nats-io/nats.go",
    sum = "h1:ik3HbLhZ0YABLto7iX80pZLPw/6dx3T+++MZJwLnMrQ=",
    version = "v1.9.1",
)

go_repository(
    name = "com_github_nats_io_nats_server_v2",
    importpath = "github.com/nats-io/nats-server/v2",
    sum = "h1:i2Ly0B+1+rzNZHHWtD4ZwKi+OU5l+uQo1iDHZ2PmiIc=",
    version = "v2.1.2",
)

go_repository(
    name = "com_github_nats_io_nkeys",
    importpath = "github.com/nats-io/nkeys",
    sum = "h1:6JrEfig+HzTH85yxzhSVbjHRJv9cn0p6n3IngIcM5/k=",
    version = "v0.1.3",
)

go_repository(
    name = "com_github_nats_io_nuid",
    importpath = "github.com/nats-io/nuid",
    sum = "h1:5iA8DT8V7q8WK2EScv2padNa/rTESc1KdnPw4TC2paw=",
    version = "v1.0.1",
)

go_repository(
    name = "com_github_neelance_parallel",
    importpath = "github.com/neelance/parallel",
    sum = "h1:NZOii9TDGRAfCS5VM16XnF4K7afoLQmIiZX8EkKnxtE=",
    version = "v0.0.0-20160708114440-4de9ce63d14c",
)

go_repository(
    name = "com_github_oklog_oklog",
    importpath = "github.com/oklog/oklog",
    sum = "h1:wVfs8F+in6nTBMkA7CbRw+zZMIB7nNM825cM1wuzoTk=",
    version = "v0.3.2",
)

go_repository(
    name = "com_github_oklog_run",
    importpath = "github.com/oklog/run",
    sum = "h1:Ru7dDtJNOyC66gQ5dQmaCa0qIsAUFY3sFpK1Xk8igrw=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_opentracing_basictracer_go",
    importpath = "github.com/opentracing/basictracer-go",
    sum = "h1:Oa1fTSBvAl8pa3U+IJYqrKm0NALwH9OsgwOqDv4xJW0=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_opentracing_contrib_go_observer",
    importpath = "github.com/opentracing-contrib/go-observer",
    sum = "h1:lM6RxxfUMrYL/f8bWEUqdXrANWtrL7Nndbm9iFN0DlU=",
    version = "v0.0.0-20170622124052-a52f23424492",
)

go_repository(
    name = "com_github_openzipkin_contrib_zipkin_go_opentracing",
    importpath = "github.com/openzipkin-contrib/zipkin-go-opentracing",
    sum = "h1:ZCnq+JUrvXcDVhX/xRolRBZifmabN1HcS1wrPSvxhrU=",
    version = "v0.4.5",
)

go_repository(
    name = "com_github_openzipkin_zipkin_go",
    importpath = "github.com/openzipkin/zipkin-go",
    sum = "h1:nY8Hti+WKaP0cRsSeQ026wU03QsM762XBeCXBb9NAWI=",
    version = "v0.2.2",
)

go_repository(
    name = "com_github_pact_foundation_pact_go",
    importpath = "github.com/pact-foundation/pact-go",
    sum = "h1:OYkFijGHoZAYbOIb1LWXrwKQbMMRUv1oQ89blD2Mh2Q=",
    version = "v1.0.4",
)

go_repository(
    name = "com_github_performancecopilot_speed",
    importpath = "github.com/performancecopilot/speed",
    sum = "h1:2WnRzIquHa5QxaJKShDkLM+sc0JPuwhXzK8OYOyt3Vg=",
    version = "v3.0.0+incompatible",
)

go_repository(
    name = "com_github_pierrec_lz4",
    importpath = "github.com/pierrec/lz4",
    sum = "h1:2xWsjqPFWcplujydGg4WmhC/6fZqK42wMM8aXeqhl0I=",
    version = "v2.0.5+incompatible",
)

go_repository(
    name = "com_github_pkg_profile",
    importpath = "github.com/pkg/profile",
    sum = "h1:F++O52m40owAmADcojzM+9gyjmMOY/T4oYJkgFDH8RE=",
    version = "v1.2.1",
)

go_repository(
    name = "com_github_rcrowley_go_metrics",
    importpath = "github.com/rcrowley/go-metrics",
    sum = "h1:9ZKAASQSHhDYGoxY8uLVpewe1GDZ2vu2Tr/vTdVAkFQ=",
    version = "v0.0.0-20181016184325-3113b8401b8a",
)

go_repository(
    name = "com_github_samuel_go_zookeeper",
    importpath = "github.com/samuel/go-zookeeper",
    sum = "h1:p3Vo3i64TCLY7gIfzeQaUJ+kppEO5WQG3cL8iE8tGHU=",
    version = "v0.0.0-20190923202752-2cc03de413da",
)

go_repository(
    name = "com_github_shirou_gopsutil_v3",
    importpath = "github.com/shirou/gopsutil/v3",
    sum = "h1:dA72XXj5WOXIZkAL2iYTKRVcNOOqh4yfLn9Rm7t8BMM=",
    version = "v3.21.1",
)

go_repository(
    name = "com_github_shopify_sarama",
    importpath = "github.com/Shopify/sarama",
    sum = "h1:9oksLxC6uxVPHPVYUmq6xhr1BOF/hHobWH2UzO67z1s=",
    version = "v1.19.0",
)

go_repository(
    name = "com_github_shopify_toxiproxy",
    importpath = "github.com/Shopify/toxiproxy",
    sum = "h1:TKdv8HiTLgE5wdJuEML90aBgNWsokNbMijUGhmcoBJc=",
    version = "v2.1.4+incompatible",
)

go_repository(
    name = "com_github_slimsag_godocmd",
    importpath = "github.com/slimsag/godocmd",
    sum = "h1:sAARUcYbwxnebBeWHzKX2MeyXtzy25TEglCTz9BhueY=",
    version = "v0.0.0-20161025000126-a1005ad29fe3",
)

go_repository(
    name = "com_github_sony_gobreaker",
    importpath = "github.com/sony/gobreaker",
    sum = "h1:oMnRNZXX5j85zso6xCPRNPtmAycat+WcoKbklScLDgQ=",
    version = "v0.4.1",
)

go_repository(
    name = "com_github_sourcegraph_ctxvfs",
    importpath = "github.com/sourcegraph/ctxvfs",
    sum = "h1:v4/JVxZSPWifxmICRqgXK7khThjw03RfdGhyeA2S4EQ=",
    version = "v0.0.0-20180418081416-2b65f1b1ea81",
)

go_repository(
    name = "com_github_sourcegraph_go_langserver",
    importpath = "github.com/sourcegraph/go-langserver",
    sum = "h1:lj2sRU7ZMIkW372IDVGb6fE8VAY4c/EMsiDzrB9vmiU=",
    version = "v2.0.0+incompatible",
)

go_repository(
    name = "com_github_sourcegraph_jsonrpc2",
    importpath = "github.com/sourcegraph/jsonrpc2",
    sum = "h1:marA1XQDC7N870zmSFIoHZpIUduK80USeY0Rkuflgp4=",
    version = "v0.0.0-20200429184054-15c2290dcb37",
)

go_repository(
    name = "com_github_streadway_amqp",
    importpath = "github.com/streadway/amqp",
    sum = "h1:WhxRHzgeVGETMlmVfqhRn8RIeeNoPr2Czh33I4Zdccw=",
    version = "v0.0.0-20190827072141-edfb9018d271",
)

go_repository(
    name = "com_github_streadway_handy",
    importpath = "github.com/streadway/handy",
    sum = "h1:AhmOdSHeswKHBjhsLs/7+1voOxT+LLrSk/Nxvk35fug=",
    version = "v0.0.0-20190108123426-d5acb3125c2a",
)

go_repository(
    name = "com_github_vividcortex_gohistogram",
    importpath = "github.com/VividCortex/gohistogram",
    sum = "h1:6+hBz+qvs0JOrrNhhmR7lFxo5sINxBCGXrdtl/UvroE=",
    version = "v1.0.0",
)

go_repository(
    name = "com_sourcegraph_sourcegraph_appdash",
    importpath = "sourcegraph.com/sourcegraph/appdash",
    sum = "h1:ucqkfpjg9WzSUubAO62csmucvxl4/JeW3F4I4909XkM=",
    version = "v0.0.0-20190731080439-ebfcffb1b5c0",
)

go_repository(
    name = "io_k8s_klog_v2",
    importpath = "k8s.io/klog/v2",
    sum = "h1:7+X0fUguPyrKEC4WjH8iGDg3laWgMo5tMnRTIGTTxGQ=",
    version = "v2.4.0",
)

go_repository(
    name = "io_k8s_sigs_apiserver_network_proxy_konnectivity_client",
    importpath = "sigs.k8s.io/apiserver-network-proxy/konnectivity-client",
    sum = "h1:uuHDyjllyzRyCIvvn0OBjiRB0SgBZGqHNYAmjR7fO50=",
    version = "v0.0.7",
)

go_repository(
    name = "io_k8s_sigs_structured_merge_diff_v3",
    importpath = "sigs.k8s.io/structured-merge-diff/v3",
    sum = "h1:dOmIZBMfhcHS09XZkMyUgkq5trg3/jRyJYFZUiaOp8E=",
    version = "v3.0.0",
)

go_repository(
    name = "io_k8s_sigs_structured_merge_diff_v4",
    importpath = "sigs.k8s.io/structured-merge-diff/v4",
    sum = "h1:YHQV7Dajm86OuqnIR6zAelnDWBRjo+YhYV9PmGrh1s8=",
    version = "v4.0.2",
)

go_repository(
    name = "com_github_gobuffalo_gitgen",
    importpath = "github.com/gobuffalo/gitgen",
    sum = "h1:mSVZ4vj4khv+oThUfS+SQU3UuFIZ5Zo6UNcvK8E8Mz8=",
    version = "v0.0.0-20190315122116-cc086187d211",
)

go_repository(
    name = "com_github_gobuffalo_gogen",
    importpath = "github.com/gobuffalo/gogen",
    sum = "h1:dLg+zb+uOyd/mKeQUYIbwbNmfRsr9hd/WtYWepmayhI=",
    version = "v0.1.1",
)

go_repository(
    name = "com_github_gobuffalo_logger",
    importpath = "github.com/gobuffalo/logger",
    sum = "h1:8thhT+kUJMTMy3HlX4+y9Da+BNJck+p109tqqKp7WDs=",
    version = "v0.0.0-20190315122211-86e12af44bc2",
)

go_repository(
    name = "com_github_gobuffalo_mapi",
    importpath = "github.com/gobuffalo/mapi",
    sum = "h1:fq9WcL1BYrm36SzK6+aAnZ8hcp+SrmnDyAxhNx8dvJk=",
    version = "v1.0.2",
)

go_repository(
    name = "com_github_gobuffalo_packd",
    importpath = "github.com/gobuffalo/packd",
    sum = "h1:4sGKOD8yaYJ+dek1FDkwcxCHA40M4kfKgFHx8N2kwbU=",
    version = "v0.1.0",
)

go_repository(
    name = "com_github_gobuffalo_packr_v2",
    importpath = "github.com/gobuffalo/packr/v2",
    sum = "h1:Ir9W9XIm9j7bhhkKE9cokvtTl1vBm62A/fene/ZCj6A=",
    version = "v2.2.0",
)

go_repository(
    name = "com_github_gobuffalo_syncx",
    importpath = "github.com/gobuffalo/syncx",
    sum = "h1:tpom+2CJmpzAWj5/VEHync2rJGi+epHNIeRSWjzGA+4=",
    version = "v0.0.0-20190224160051-33c29581e754",
)

go_repository(
    name = "com_github_jinzhu_inflection",
    importpath = "github.com/jinzhu/inflection",
    sum = "h1:K317FqzuhWc8YvSVlFMCCUb36O/S9MCKRDI7QkRKD/E=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_kamva_mgm_v3",
    importpath = "github.com/kamva/mgm/v3",
    sum = "h1:WLMrXwPXXGeeqvd+xg5XnQ0o1QuPkb3e4gL2sZXCxw0=",
    version = "v3.1.0",
)

go_repository(
    name = "com_github_karrick_godirwalk",
    importpath = "github.com/karrick/godirwalk",
    sum = "h1:lOpSw2vJP0y5eLBW906QwKsUK/fe/QDyoqM5rnnuPDY=",
    version = "v1.10.3",
)

go_repository(
    name = "com_github_markbates_oncer",
    importpath = "github.com/markbates/oncer",
    sum = "h1:JgVTCPf0uBVcUSWpyXmGpgOc62nK5HWUBKAGc3Qqa5k=",
    version = "v0.0.0-20181203154359-bf2de49a0be2",
)

go_repository(
    name = "com_github_markbates_safe",
    importpath = "github.com/markbates/safe",
    sum = "h1:yjZkbvRM6IzKj9tlu/zMJLS0n/V351OZWRnF3QfaUxI=",
    version = "v1.0.1",
)

go_repository(
    name = "com_github_montanaflynn_stats",
    importpath = "github.com/montanaflynn/stats",
    sum = "h1:iruDEfMl2E6fbMZ9s0scYfZQ84/6SPL6zC8ACM2oIL0=",
    version = "v0.0.0-20171201202039-1bf9dbcd8cbe",
)

go_repository(
    name = "com_github_tidwall_pretty",
    importpath = "github.com/tidwall/pretty",
    sum = "h1:HsD+QiTn7sK6flMKIvNmpqz1qrpP3Ps6jOKIKMooyg4=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_xdg_scram",
    importpath = "github.com/xdg/scram",
    sum = "h1:u40Z8hqBAAQyv+vATcGgV0YCnDjqSL7/q/JyPhhJSPk=",
    version = "v0.0.0-20180814205039-7eeb5667e42c",
)

go_repository(
    name = "com_github_xdg_stringprep",
    importpath = "github.com/xdg/stringprep",
    sum = "h1:d9X0esnoa3dFsV0FG35rAT0RIhYFlPq7MiP+DW89La0=",
    version = "v1.0.0",
)

go_repository(
    name = "org_mongodb_go_mongo_driver",
    importpath = "go.mongodb.org/mongo-driver",
    sum = "h1:zs/dKNwX0gYUtzwrN9lLiR15hCO0nDwQj5xXx+vjCdE=",
    version = "v1.3.4",
)

go_repository(
    name = "com_github_linkedin_goavro_v2",
    importpath = "github.com/linkedin/goavro/v2",
    sum = "h1:eTBIRoInBM88gITGXYtUSqqxLTFXfOsJBiX8ZMW0o4U=",
    version = "v2.10.0",
)

go_repository(
    name = "com_github_robbiet480_go_sns",
    importpath = "github.com/robbiet480/go.sns",
    sum = "h1:oiuVamdP4LloTcrinlnYOxhLwhJCV3hE9D+NSxH0L4I=",
    version = "v0.0.0-20181124163742-ca087b49e1da",
)

go_repository(
    name = "com_github_git_chglog_git_chglog",
    importpath = "github.com/git-chglog/git-chglog",
    sum = "h1:8l4Aw3Jmx0pLKYMkY+1b6yBPgE+rzRtA5T3vqFyI2Z8=",
    version = "v0.0.0-20190611050339-63a4e637021f",
)

go_repository(
    name = "com_github_gobuffalo_attrs",
    importpath = "github.com/gobuffalo/attrs",
    sum = "h1:hSkbZ9XSyjyBirMeqSqUrK+9HboWrweVlzRNqoBi2d4=",
    version = "v0.0.0-20190224210810-a9411de4debd",
)

go_repository(
    name = "com_github_gobuffalo_depgen",
    importpath = "github.com/gobuffalo/depgen",
    sum = "h1:31atYa/UW9V5q8vMJ+W6wd64OaaTHUrCUXER358zLM4=",
    version = "v0.1.0",
)

go_repository(
    name = "com_github_gobuffalo_envy",
    importpath = "github.com/gobuffalo/envy",
    sum = "h1:GlXgaiBkmrYMHco6t4j7SacKO4XUjvh5pwXh0f4uxXU=",
    version = "v1.7.0",
)

go_repository(
    name = "com_github_gobuffalo_flect",
    importpath = "github.com/gobuffalo/flect",
    sum = "h1:3GQ53z7E3o00C/yy7Ko8VXqQXoJGLkrTQCLTF1EjoXU=",
    version = "v0.1.3",
)

go_repository(
    name = "com_github_gobuffalo_genny",
    importpath = "github.com/gobuffalo/genny",
    sum = "h1:iQ0D6SpNXIxu52WESsD+KoQ7af2e3nCfnSBoSF/hKe0=",
    version = "v0.1.1",
)

go_repository(
    name = "com_github_hinshun_vt10x",
    importpath = "github.com/hinshun/vt10x",
    sum = "h1:WlZsjVhE8Af9IcZDGgJGQpNflI3+MJSBhsgT5PCtzBQ=",
    version = "v0.0.0-20180616224451-1954e6464174",
)

go_repository(
    name = "com_github_kballard_go_shellquote",
    importpath = "github.com/kballard/go-shellquote",
    sum = "h1:Z9n2FFNUXsshfwJMBgNA0RU6/i7WVaAegv3PtuIHPMs=",
    version = "v0.0.0-20180428030007-95032a82bc51",
)

go_repository(
    name = "com_github_netflix_go_expect",
    importpath = "github.com/Netflix/go-expect",
    sum = "h1:xzYJEypr/85nBpB11F9br+3HUrpgb+fcm5iADzXXYEw=",
    version = "v0.0.0-20180615182759-c93bf25de8e8",
)

go_repository(
    name = "com_github_robinjoseph08_redisqueue",
    importpath = "github.com/robinjoseph08/redisqueue",
    sum = "h1:8pcVNqJNYuuZkc4z+mKFq06AA1I1xD3mo3x9yKeBuEQ=",
    version = "v1.1.0",
)

go_repository(
    name = "com_github_tsuyoshiwada_go_gitcmd",
    importpath = "github.com/tsuyoshiwada/go-gitcmd",
    sum = "h1:Y2l28Jr3vOEeYtxfVbMtVfOdAwuUqWaP9fvNKiBVeXY=",
    version = "v0.0.0-20180205145712-5f1f5f9475df",
)

go_repository(
    name = "in_gopkg_alecaivazis_survey_v1",
    importpath = "gopkg.in/AlecAivazis/survey.v1",
    sum = "h1:QoEEmn/d5BbuPIL2qvXwzJdttFFhRQFkaq+tEKb7SMI=",
    version = "v1.8.5",
)

go_repository(
    name = "in_gopkg_kyokomi_emoji_v1",
    importpath = "gopkg.in/kyokomi/emoji.v1",
    sum = "h1:beetH5mWDMzFznJ+Qzd5KVHp79YKhVUMcdO8LpRLeGw=",
    version = "v1.5.1",
)

go_repository(
    name = "com_github_blendle_zapdriver",
    importpath = "github.com/blendle/zapdriver",
    sum = "h1:C3dydBOWYRiOk+B8X9IVZ5IOe+7cl+tGOexN4QqHfpE=",
    version = "v1.3.1",
)

go_rules_dependencies()

go_register_toolchains(version = "1.19.8")

gazelle_dependencies()

http_archive(
    name = "rules_proto_grpc",
    sha256 = "5f0f2fc0199810c65a2de148a52ba0aff14d631d4e8202f41aff6a9d590a471b",
    strip_prefix = "rules_proto_grpc-1.0.2",
    urls = ["https://harness-artifactory.harness.io/artifactory/rules-proto-grpc-github/archive/1.0.2.tar.gz"],
)

load("@rules_proto_grpc//:repositories.bzl", "rules_proto_grpc_repos", "rules_proto_grpc_toolchains")

rules_proto_grpc_toolchains()

rules_proto_grpc_repos()

load("@rules_proto_grpc//java:repositories.bzl", rules_proto_grpc_java_repos = "java_repos")

rules_proto_grpc_java_repos()

load("@io_grpc_grpc_java//:repositories.bzl", "grpc_java_repositories")

grpc_java_repositories()

http_archive(
    name = "com_github_query_builder_generator",
    sha256 = "d72449d0ed7848260c6421be3677633946de46dc69af6588dbb4bc938e9714db",
    strip_prefix = "query-builder-generator-0.1.23",
    urls = ["https://github.com/wings-software/query-builder-generator/archive/refs/tags/v0.1.23.zip"],
)

go_repository(
    name = "org_golang_google_protobuf",
    importpath = "google.golang.org/protobuf",
    sum = "h1:4MY060fB1DLGMB/7MBTLnwQUY6+F09GEiz6SsrNqyzM=",
    version = "v1.23.0",
)

###########################################################################################
########################################   Java code ######################################

http_archive(
    name = "rules_spring",
    sha256 = "9385652bb92d365675d1ca7c963672a8091dc5940a9e307104d3c92e7a789c8e",
    urls = [
        "https://harness-artifactory.harness.io/artifactory/rules-spring-github/download/2.1.4/rules-spring-2.1.4.zip",
    ],
)

RULES_JVM_EXTERNAL_TAG = "4.1"

http_archive(
    name = "rules_jvm_external",
    sha256 = "f36441aa876c4f6427bfb2d1f2d723b48e9d930b62662bf723ddfb8fc80f0140",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://harness-artifactory.harness.io/artifactory/rules-jvm-external-github/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@rules_jvm_external//:specs.bzl", "maven")

plain_artifacts = [
    "aopalliance:aopalliance:1.0",
    "cglib:cglib-nodep:3.1",
    "ch.qos.logback.contrib:logback-jackson:0.1.5",
    "ch.qos.logback.contrib:logback-json-classic:0.1.5",
    "ch.qos.logback.contrib:logback-json-core:0.1.5",
    "ch.qos.logback:logback-access:1.2.11",
    "ch.qos.logback:logback-classic:1.2.11",
    "ch.qos.logback:logback-core:1.2.11",
    "com.auth0:java-jwt:3.1.0",
    "com.azure:azure-core:1.29.1",
    "com.azure:azure-identity:1.5.0",
    "com.azure:azure-core-http-netty:1.11.9",
    "com.azure:azure-core-http-okhttp:1.10.1",
    "com.azure:azure-containers-containerregistry:1.0.3",
    "com.azure:azure-core-management:1.6.2",
    "com.azure.resourcemanager:azure-resourcemanager:2.14.0",
    "com.azure.resourcemanager:azure-resourcemanager-appservice:2.14.0",
    "com.azure.resourcemanager:azure-resourcemanager-authorization:2.14.0",
    "com.azure.resourcemanager:azure-resourcemanager-compute:2.14.0",
    "com.azure.resourcemanager:azure-resourcemanager-containerinstance:2.14.0",
    "com.azure.resourcemanager:azure-resourcemanager-containerregistry:2.14.0",
    "com.azure.resourcemanager:azure-resourcemanager-containerservice:2.14.0",
    "com.azure.resourcemanager:azure-resourcemanager-keyvault:2.14.0",
    "com.azure.resourcemanager:azure-resourcemanager-msi:2.14.0",
    "com.azure.resourcemanager:azure-resourcemanager-monitor:2.14.0",
    "com.azure.resourcemanager:azure-resourcemanager-network:2.14.0",
    "com.azure.resourcemanager:azure-resourcemanager-resources:2.14.0",
    "com.azure.resourcemanager:azure-resourcemanager-storage:2.14.0",
    "com.azure:azure-security-keyvault-keys:4.4.1",
    "com.azure:azure-security-keyvault-secrets:4.4.1",
    "com.azure:azure-security-keyvault-administration:4.1.2",
    "com.azure:azure-storage-blob:12.16.0",
    "com.azure:azure-storage-common:12.15.1",
    "com.bettercloud:vault-java-driver:4.0.0",
    "com.rabbitmq:amqp-client:jar:4.8.0",
    "com.bugsnag:bugsnag:3.6.2",
    "com.carrotsearch:hppc:0.8.1",
    "com.cronutils:cron-utils:9.1.6",
    "com.datadoghq:java-dogstatsd-client:2.3",
    "com.deftlabs:mongo-java-distributed-lock:0.1.7",
    "com.eclipsesource.j2v8:j2v8_macosx_x86_64:4.6.0",
    "com.esotericsoftware.yamlbeans:yamlbeans:1.13",
    "com.esotericsoftware:kryo:4.0.2",
    "com.esotericsoftware:minlog:1.3.0",
    "com.esotericsoftware:reflectasm:1.11.6",
    "com.fasterxml.jackson.core:jackson-annotations:2.14.2",
    "com.fasterxml.jackson.core:jackson-core:2.14.2",
    "com.fasterxml.jackson.core:jackson-databind:2.13.4.2",
    "com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:2.14.2",
    "com.fasterxml.jackson.dataformat:jackson-dataformat-smile:2.14.2",
    "com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.2",
    "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2",
    "com.fasterxml.jackson.datatype:jackson-datatype-guava:2.14.2",
    "com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.14.2",
    "com.fasterxml.jackson.datatype:jackson-datatype-joda:2.14.2",
    "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2",
    "com.fasterxml.jackson.jaxrs:jackson-jaxrs-base:2.14.2",
    "com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider:2.14.2",
    "com.fasterxml.jackson.jaxrs:jackson-jaxrs-yaml-provider:2.14.2",
    "com.fasterxml.jackson.module:jackson-module-afterburner:2.14.2",
    "com.fasterxml.jackson.module:jackson-module-jaxb-annotations:2.14.2",
    "com.fasterxml.jackson.module:jackson-module-jsonSchema:2.14.2",
    "com.fasterxml.jackson.module:jackson-module-parameter-names:2.14.2",
    "com.fasterxml.uuid:java-uuid-generator:4.0.1",
    "com.fasterxml.woodstox:woodstox-core:6.4.0",
    "com.fasterxml:classmate:1.4.0",
    "com.github.allbegray:slack-api:v1.3.0.RELEASE",
    "com.github.ben-manes.caffeine:caffeine:2.8.4",
    "com.github.ben-manes.caffeine:jcache:2.8.0",
    "com.github.daniel-shuy:kafka-protobuf-serde:2.2.0",
    "com.github.daniel-shuy:kafka-protobuf-serde:2.2.0",
    "com.github.dikhan:pagerduty-client:3.0.3",
    "com.github.dirkraft.dropwizard-file-assets:dropwizard-file-assets:0.0.2",
    "com.github.fge:json-schema-core:1.0.1",
    "com.github.heremaps:oksse:0.9.0",
    "com.github.javaparser:javaparser-core:3.22.1",
    "com.github.javaparser:javaparser-symbol-solver-core:3.22.1",
    "com.github.luben:zstd-jni:1.4.0-1",
    "com.github.luben:zstd-jni:1.4.0-1",
    "com.github.mifmif:generex:1.0.1",
    "com.github.rutledgepaulv:rest-query-engine:0.7.1",
    "com.github.scribejava:scribejava-apis:6.2.0",
    "com.github.scribejava:scribejava-core:6.2.0",
    "com.github.spullara.mustache.java:compiler:0.9.5",
    "com.github.stephenc.jcip:jcip-annotations:1.0-1",
    "com.github.vladimir-bukhtoyarov:bucket4j-core:4.8.0",
    "com.github.zafarkhaja:java-semver:0.9.0",
    "com.google.android:annotations:4.1.1.4",
    "com.google.api-client:google-api-client:1.25.0",
    "com.google.api.grpc:proto-google-cloud-datastore-v1:0.62.0",
    "com.google.api.grpc:proto-google-cloud-iamcredentials-v1:1.0.2",
    "com.google.api.grpc:proto-google-cloud-kms-v1:0.79.0",
    "com.google.api.grpc:proto-google-cloud-logging-v2:0.76.0",
    "com.google.api.grpc:proto-google-cloud-pubsub-v1:1.89.0",
    "com.google.api.grpc:proto-google-cloud-secretmanager-v1:2.5.0",
    "com.google.api.grpc:proto-google-common-protos:2.9.6",
    "com.google.api.grpc:proto-google-iam-v1:0.13.0",
    "com.google.api:api-common:1.8.1",
    "com.google.api:gax-grpc:2.18.7",
    "com.google.api:gax-httpjson:0.105.0",
    "com.google.api:gax:2.18.7",
    "com.google.apis:google-api-services-bigquery:v2-rev20191211-1.30.3",
    "com.google.apis:google-api-services-cloudbilling:v1-rev56-1.25.0",
    "com.google.apis:google-api-services-cloudcommerceprocurement:v1-rev20190531-1.25.0",
    "com.google.apis:google-api-services-cloudresourcemanager:v1-rev20200210-1.30.9",
    "com.google.apis:google-api-services-compute:v1-rev213-1.25.0",
    "com.google.apis:google-api-services-iam:v1-rev314-1.25.0",
    "com.google.apis:google-api-services-logging:v2-rev631-1.25.0",
    "com.google.apis:google-api-services-monitoring:v3-rev477-1.25.0",
    "com.google.apis:google-api-services-servicecontrol:v1-rev142-1.25.0",
    "com.google.apis:google-api-services-storage:v1-rev131-1.22.0",
    "com.google.auth:google-auth-library-credentials:0.18.0",
    "com.google.auth:google-auth-library-oauth2-http:0.20.0",
    "com.google.auto.service:auto-service:1.0-rc6",
    "com.google.auto.value:auto-value-annotations:1.7.4",
    "com.google.cloud.datastore:datastore-v1-proto-client:1.6.0",
    "com.google.cloud:google-cloud-bigquery:1.106.0",
    "com.google.cloud:google-cloud-bigquerydatatransfer:0.126.0-beta",
    "com.google.cloud:google-cloud-core-grpc:1.93.7",
    "com.google.cloud:google-cloud-core-http:1.92.4",
    "com.google.cloud:google-cloud-core:1.92.4",
    "com.google.cloud:google-cloud-datastore:1.79.0",
    "com.google.cloud:google-cloud-iamcredentials:1.0.2",
    "com.google.cloud:google-cloud-kms:1.32.0",
    "com.google.cloud:google-cloud-logging:1.93.0",
    "com.google.cloud:google-cloud-pubsub:1.107.0",
    "com.google.cloud:google-cloud-resourcemanager:0.117.2-alpha",
    "com.google.cloud:google-cloud-secretmanager:1.2.8",
    "com.google.cloud:google-cloud-storage:1.52.0",
    "com.google.cloud:google-cloud-functions:2.8.0",
    "com.google.cloud:google-cloud-run:0.6.0",
    "com.google.api.grpc:proto-google-cloud-run-v2:0.6.0",
    "com.google.code.findbugs:annotations:3.0.0",
    "com.google.code.findbugs:jsr305:3.0.2",
    "com.google.code.gson:gson:2.8.9",
    "com.google.errorprone:error_prone_annotations:2.3.4",
    "com.google.guava:guava:31.1-jre",
    "com.google.http-client:google-http-client-apache-v2:1.38.0",
    "com.google.http-client:google-http-client-appengine:1.34.1",
    "com.google.http-client:google-http-client-jackson2:1.34.1",
    "com.google.http-client:google-http-client-jackson:1.20.0",
    "com.google.http-client:google-http-client-protobuf:1.20.0",
    "com.google.http-client:google-http-client:1.34.1",
    "com.google.inject.extensions:guice-assistedinject:5.1.0",
    "com.google.inject.extensions:guice-multibindings:4.2.3",
    "com.google.inject:guice:5.1.0",
    "com.google.j2objc:j2objc-annotations:1.3",
    "com.google.oauth-client:google-oauth-client:1.34.0",
    "com.google.protobuf:protobuf-java-util:3.21.7",
    "com.google.protobuf:protobuf-java:3.21.7",
    "com.googlecode.javaewah:JavaEWAH:1.1.6",
    "com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20211018.1",
    "com.graphql-java:graphql-java:12.0",
    "com.graphql-java:java-dataloader:2.1.1",
    "com.healthmarketscience.sqlbuilder:sqlbuilder:3.0.0",
    "com.helger:profiler:1.1.1",
    "com.hierynomus:asn-one:0.6.0",
    "com.hierynomus:smbj:0.11.5",
    "com.hierynomus:sshj:0.34.0",
    "com.hubspot.jackson:jackson-datatype-protobuf:0.9.11-jackson2.7",
    "com.intellij:annotations:12.0",
    "com.j256.two-factor-auth:two-factor-auth:1.0",
    "com.jakewharton.retrofit:retrofit1-okhttp3-client:1.1.0",
    "com.jakewharton.retrofit:retrofit2-reactor-adapter:2.1.0",
    "com.jayway.jsonpath:json-path:2.2.0",
    "com.jcraft.harness:jsch-0-1-54-harness-patch:1.1",
    "com.jcraft:jzlib:1.1.3",
    "com.lmax:disruptor:jar:3.4.2",
    "com.mashape.unirest:unirest-java:1.4.9",
    "com.mchange:c3p0:0.9.5.4",
    "com.mchange:mchange-commons-java:0.2.15",
    "com.microsoft.azure:msal4j:1.11.0",
    "com.networknt:json-schema-validator:1.0.54",
    "com.nimbusds:lang-tag:1.5",
    "com.nimbusds:nimbus-jose-jwt:8.19",
    "com.nimbusds:oauth2-oidc-sdk:7.4",
    "org.asynchttpclient:async-http-client:2.12.3",
    "com.novemberain:quartz-mongodb:2.2.0-rc2",
    "com.offbytwo.jenkins:jenkins-client:0.3.9",
    "com.openpojo:openpojo:0.8.3",
    "com.palominolabs.metrics:metrics-guice:3.2.0",
    "com.papertrail:profiler:1.0.2",
    "com.segment.analytics.java:analytics-core:2.1.1",
    "com.segment.analytics.java:analytics:2.1.1",
    "com.segment.backo:backo:1.0.0",
    "com.slack.api:slack-api-client:1.5.3",
    "com.slack.api:slack-api-model:1.5.3",
    "com.smoketurner:dropwizard-swagger:2.0.12-1",
    "com.splunk:splunk:1.6.3.0",
    "com.spotify:docker-client:8.16.0",
    "com.spotify:docker-client:8.16.0",
    "com.squareup.okhttp3:logging-interceptor:4.9.2",
    "com.squareup.okhttp3:okhttp-urlconnection:4.9.2",
    "com.squareup.okhttp3:okhttp:4.9.2",
    "com.squareup.okhttp3:mockwebserver:4.9.2",
    "com.squareup.okio:okio:2.8.0",
    "com.squareup.retrofit2:converter-protobuf:2.9.0",
    "com.squareup.retrofit2:adapter-rxjava:2.7.2",
    "com.squareup.retrofit2:converter-gson:2.3.0",
    "com.squareup.retrofit2:converter-jackson:2.6.0",
    "com.squareup.retrofit2:converter-jaxb:jar:2.9.0",
    "com.squareup.retrofit2:retrofit:2.6.0",
    "com.squareup.retrofit2:converter-scalars:2.9.0",
    "com.squareup.wire:wire-runtime:2.2.0",
    "com.squareup.wire:wire-schema:3.2.2",
    "com.squareup.wire:wire-schema:3.2.2",
    "com.stripe:stripe-java:20.133.0",
    "com.sumologic.api.client:sumo-java-client:2.5",
    "com.sun.activation:jakarta.activation:1.2.2",
    "com.sun.istack:istack-commons-runtime:3.0.8",
    "com.sun.mail:javax.mail:1.6.0",
    "com.sun.xml.fastinfoset:FastInfoset:1.2.16",
    "com.sun.xml.messaging.saaj:saaj-impl:1.4.0-b03",
    "com.tdunning:t-digest:3.2",
    "com.thoughtworks.proxytoys:proxytoys:1.0",
    "com.typesafe:config:1.3.4",
    "com.zaxxer:HikariCP-java7:2.4.13",
    "commons-beanutils:commons-beanutils:1.9.4",
    "commons-cli:commons-cli:1.5.0",
    "commons-codec:commons-codec:1.15",
    "commons-collections:commons-collections:3.2.2",
    "commons-dbcp:commons-dbcp:1.4",
    "commons-digester:commons-digester:2.1",
    "commons-io:commons-io:2.7",
    "commons-lang:commons-lang:2.3",
    "commons-logging:commons-logging:1.1.1",
    "commons-net:commons-net:3.9.0",
    "commons-pool:commons-pool:1.5.4",
    "commons-validator:commons-validator:1.7",
    "de.danielbechler:java-object-diff:0.94",
    "de.javakaffee:kryo-serializers:0.41",
    "de.jkeylockmanager:jkeylockmanager:2.1.0",
    "de.skuzzle:semantic-version:2.1.0",
    "dk.brics.automaton:automaton:1.11-8",
    "dnsjava:dnsjava:2.1.8",
    "es.moki.ratelimitj:ratelimitj-core:0.6.0",
    "es.moki.ratelimitj:ratelimitj-inmemory:0.6.0",
    "guru.nidi.com.kitfox:svgSalamander:1.1.3",
    "guru.nidi:graphviz-java:0.16.3",
    "guru.nidi:graphviz-rough:0.16.3",
    "io.cloudsoft.windows:winrm4j-client:0.12.3",
    "io.cloudsoft.windows:winrm4j:0.12.3",
    "io.confluent:common-config:5.5.1",
    "io.confluent:common-config:5.5.1",
    "io.confluent:common-utils:5.5.1",
    "io.confluent:common-utils:5.5.1",
    "io.confluent:kafka-protobuf-provider:5.5.1",
    "io.confluent:kafka-protobuf-provider:5.5.1",
    "io.confluent:kafka-protobuf-serializer:5.5.1",
    "io.confluent:kafka-protobuf-serializer:5.5.1",
    "io.confluent:kafka-schema-registry-client:5.5.1",
    "io.confluent:kafka-schema-registry-client:5.5.1",
    "io.confluent:kafka-schema-serializer:5.5.1",
    "io.confluent:kafka-schema-serializer:5.5.1",
    "io.debezium:debezium-api:1.7.2.Final",
    "io.debezium:debezium-connector-mongodb:1.7.2.Final",
    "io.debezium:debezium-core:1.7.2.Final",
    "io.dropwizard-bundles:dropwizard-configurable-assets-bundle:1.3.5",
    "io.dropwizard.metrics:metrics-annotation:4.1.19",
    "io.dropwizard.metrics:metrics-core:4.1.19",
    "io.dropwizard.metrics:metrics-graphite:4.1.19",
    "io.dropwizard.metrics:metrics-healthchecks:4.1.19",
    "io.dropwizard.metrics:metrics-jersey2:4.1.19",
    "io.dropwizard.metrics:metrics-jetty9:4.1.19",
    "io.dropwizard.metrics:metrics-jmx:4.1.19",
    "io.dropwizard.metrics:metrics-json:4.1.19",
    "io.dropwizard.metrics:metrics-jvm:4.1.19",
    "io.dropwizard.metrics:metrics-logback:4.1.19",
    "io.dropwizard.metrics:metrics-servlets:4.1.19",
    "io.dropwizard:dropwizard-assets:2.0.21",
    "io.dropwizard:dropwizard-auth:2.0.21",
    "io.dropwizard:dropwizard-configuration:2.0.21",
    "io.dropwizard:dropwizard-core:2.0.21",
    "io.dropwizard:dropwizard-http2:2.0.21",
    "io.dropwizard:dropwizard-jackson:2.0.21",
    "io.dropwizard:dropwizard-jersey:2.0.21",
    "io.dropwizard:dropwizard-jetty:2.0.21",
    "io.dropwizard:dropwizard-lifecycle:2.0.21",
    "io.dropwizard:dropwizard-logging:2.0.21",
    "io.dropwizard:dropwizard-metrics-graphite:2.0.21",
    "io.dropwizard:dropwizard-metrics:2.0.21",
    "io.dropwizard:dropwizard-request-logging:2.0.21",
    "io.dropwizard.modules:dropwizard-protobuf:2.0.12-1",
    "io.dropwizard:dropwizard-servlets:2.0.21",
    "io.dropwizard:dropwizard-util:2.0.21",
    "io.dropwizard:dropwizard-validation:2.0.21",
    "io.dropwizard:dropwizard-views-freemarker:2.0.21",
    "io.dropwizard:dropwizard-views-mustache:2.0.21",
    "io.dropwizard:dropwizard-views:2.0.21",
    "io.dropwizard.logback:logback-throttling-appender:1.1.0",
    "io.fabric8:fabric8-utils:3.0.11",
    "io.fabric8:kubernetes-api:3.0.12",
    "io.fabric8:kubernetes-client:5.12.1",
    "io.fabric8:openshift-client:5.12.1",
    "io.fabric8:kubernetes-client-project:5.12.1",
    "io.fabric8:openshift-client:5.12.1",
    "io.fabric8:kubernetes-model-generator:5.12.1",
    "io.fabric8:openshift-model:5.12.1",
    "io.fabric8:istio-client:5.12.1",
    "io.fabric8:kubernetes-extensions:5.12.1",
    "io.fabric8:istio-model-v1beta1:5.12.1",
    "io.fabric8:istio-model-v1alpha3:5.12.1",
    "io.fabric8:zjsonpatch:0.3.0",
    "io.github.benas:random-beans:3.7.0",
    "io.github.resilience4j:resilience4j-circuitbreaker:1.5.0",
    "io.github.resilience4j:resilience4j-core:1.5.0",
    "io.github.resilience4j:resilience4j-ratelimiter:1.5.0",
    "io.github.resilience4j:resilience4j-retrofit:1.5.0",
    "io.github.resilience4j:resilience4j-retry:1.5.0",
    "io.grpc:grpc-alts:1.48.0",
    "io.grpc:grpc-api:1.48.0",
    "io.grpc:grpc-auth:1.48.0",
    "io.grpc:grpc-context:1.48.0",
    "io.grpc:grpc-core:1.48.0",
    "io.grpc:grpc-grpclb:1.48.0",
    "io.grpc:grpc-netty-shaded:1.48.0",
    "io.grpc:grpc-netty:1.48.0",
    "io.grpc:grpc-protobuf-lite:1.50.1",
    "io.grpc:grpc-protobuf:1.50.1",
    "io.grpc:grpc-services:1.50.1",
    "io.grpc:grpc-stub:1.50.1",
    "io.gsonfire:gson-fire:1.8.3",
    "io.harness.cv:data-collection-dsl:0.50-RELEASE",
    "io.harness:ff-java-server-sdk:1.1.10",
    "io.jsonwebtoken:jjwt:0.9.1",
    "io.kubernetes:client-java-api:18.0.0",
    "io.kubernetes:client-java-extended:18.0.0",
    "io.kubernetes:client-java-proto:18.0.0",
    "io.kubernetes:client-java:18.0.0",
    "io.kubernetes:client-java-api-fluent:18.0.0",
    "io.leangen.graphql:spqr:0.11.2",
    "io.netty:netty-all:4.1.86.Final",
    "io.netty:netty-buffer:4.1.86.Final",
    "io.netty:netty-handler-proxy:4.1.86.Final",
    "io.netty:netty-common:4.1.86.Final",
    "io.netty:netty-handler:4.1.86.Final",
    "io.netty:netty-resolver-dns:4.1.86.Final",
    "io.netty:netty-resolver:4.1.86.Final",
    "io.netty:netty-transport-native-epoll:linux-x86_64:4.1.86.Final",
    "io.netty:netty-transport-native-kqueue:4.1.86.Final",
    "io.netty:netty-transport-native-unix-common:4.1.86.Final",
    "io.netty:netty-transport:4.1.86.Final",
    "io.opencensus:opencensus-api:0.24.0",
    "io.opencensus:opencensus-contrib-http-util:0.24.0",
    "io.opencensus:opencensus-exporter-stats-stackdriver:0.28.0",
    "io.opencensus:opencensus-exporter-stats-prometheus:0.28.0",
    "io.opencensus:opencensus-impl:0.28.0",
    "io.perfmark:perfmark-api:0.19.0",
    "io.projectreactor.netty:reactor-netty:1.0.24",
    "io.projectreactor.netty:reactor-netty-core:1.0.24",
    "io.projectreactor.netty:reactor-netty-http:1.0.24",
    "io.projectreactor:reactor-core:3.4.18",
    "io.prometheus:simpleclient:0.5.0",
    "io.prometheus:simpleclient_common:0.5.0",
    "io.prometheus:simpleclient_dropwizard:0.5.0",
    "io.prometheus:simpleclient_servlet:0.3.0",
    "io.prometheus:simpleclient_httpserver:0.4.0",
    "io.reactivex.rxjava2:rxjava:2.2.19",
    "io.reactivex:rxjava:1.3.8",
    "io.rest-assured:rest-assured:3.2.0",
    "io.rest-assured:rest-assured:3.2.0",
    "io.specto:hoverfly-java:0.13.1",
    "io.sundr:builder-annotations:0.21.0",
    "io.sundr:resourcecify-annotations:0.21.0",
    "io.sundr:sundr-codegen:0.21.0",
    "io.sundr:sundr-core:0.21.0",
    "io.vavr:vavr-match:0.9.1",
    "io.vavr:vavr:0.9.1",
    "jakarta.activation:jakarta.activation-api:1.2.2",
    "jakarta.annotation:jakarta.annotation-api:jar:1.3.5",
    "jakarta.servlet:jakarta.servlet-api:4.0.3",
    "jakarta.validation:jakarta.validation-api:2.0.2",
    "jakarta.xml.bind:jakarta.xml.bind-api:2.3.3",
    "jakarta.ws.rs:jakarta.ws.rs-api:jar:2.1.6",
    "javax.activation:activation:1.1.1",
    "javax.activation:javax.activation-api:1.2.0",
    "javax.annotation:javax.annotation-api:1.3.1",
    "javax.cache:cache-api:1.0.0",
    "javax.inject:javax.inject:1",
    "javax.servlet:javax.servlet-api:3.1.0",
    "javax.validation:validation-api:2.0.0.Final",
    "javax.ws.rs:javax.ws.rs-api:2.0.1",
    "javax.xml.bind:jaxb-api:2.3.0",
    "javax.xml.soap:javax.xml.soap-api:1.4.0",
    "javax.xml.ws:jaxws-api:2.3.1",
    "jaxen:jaxen:1.1.6",
    "javax:javaee-api:8.0",
    "joda-time:joda-time:2.10.6",
    "net.arnx:nashorn-promise:0.1.1",
    "net.bytebuddy:byte-buddy:1.14.1",
    "net.engio:mbassador:1.3.0",
    "net.i2p.crypto:eddsa:0.3.0",
    "net.java.dev.jna:jna-platform:5.5.0",
    "net.java.dev.jna:jna:5.5.0",
    "net.jcip:jcip-annotations:1.0",
    "net.jodah:expiringmap:0.5.7",
    "net.jodah:failsafe:2.2.0",
    "net.minidev:accessors-smart:1.2",
    "net.minidev:json-smart:2.4.10",
    "net.openhft:affinity:3.2.2",
    "net.openhft:chronicle-bytes:2.19.0",
    "net.openhft:chronicle-core:2.19.0",
    "net.openhft:chronicle-queue:5.19.2",
    "net.openhft:chronicle-threads:2.17.27",
    "net.openhft:chronicle-wire:2.17.71",
    "net.openhft:compiler:2.3.4",
    "net.rcarz:jira-client:0.9-SNAPSHOT",
    "net.redhogs.cronparser:cron-parser:2.6",
    "net.sf.ezmorph:ezmorph:1.0.6",
    "net.sf.jopt-simple:jopt-simple:5.0.2",
    "net.sf.json-lib:json-lib:jdk15:2.4",
    "net.sf.opencsv:opencsv:2.3",
    "net.shibboleth.utilities:java-support:7.5.0",
    "net.sourceforge.argparse4j:argparse4j:0.8.1",
    "org.antlr:antlr4-runtime:4.7.2",
    "org.apache.avro:avro:1.9.2",
    "org.apache.avro:avro:1.9.2",
    "org.apache.commons:commons-collections4:4.1",
    "org.apache.commons:commons-compress:1.21",
    "org.apache.commons:commons-csv:1.5",
    "org.apache.commons:commons-email:1.5",
    "org.apache.commons:commons-exec:1.3",
    "org.apache.commons:commons-io:1.3.2",
    "org.apache.commons:commons-jexl3:3.0",
    "org.apache.commons:commons-lang3:3.10",
    "org.apache.commons:commons-math3:3.6.1",
    "org.apache.commons:commons-text:1.10.0",
    "org.apache.cxf:cxf-core:3.5.5",
    "org.apache.cxf:cxf-rt-bindings-soap:3.5.5",
    "org.apache.cxf:cxf-rt-bindings-xml:3.5.5",
    "org.apache.cxf:cxf-rt-databinding-jaxb:3.5.5",
    "org.apache.cxf:cxf-rt-frontend-jaxws:3.5.5",
    "org.apache.cxf:cxf-rt-frontend-simple:3.5.5",
    "org.apache.cxf:cxf-rt-transports-http-hc:3.5.5",
    "org.apache.cxf:cxf-rt-transports-http:3.5.5",
    "org.apache.cxf:cxf-rt-ws-addr:3.5.5",
    "org.apache.cxf:cxf-rt-ws-policy:3.5.5",
    "org.apache.cxf:cxf-rt-wsdl:3.5.5",
    "org.apache.geronimo.specs:geronimo-jta_1.1_spec:1.1.1",
    "org.apache.geronimo.specs:geronimo-ws-metadata_2.0_spec:1.1.3",
    "org.apache.httpcomponents:fluent-hc:4.5.13",
    "org.apache.httpcomponents:httpasyncclient:4.1.4",
    "org.apache.httpcomponents:httpclient:4.5.13",
    "org.apache.httpcomponents:httpcore-nio:4.4.14",
    "org.apache.httpcomponents:httpcore:4.4.14",
    "org.apache.httpcomponents:httpmime:4.5.13",
    "org.mybatis:mybatis:jar:3.5.7",
    "org.apache.kafka:connect-api:2.8.1",
    "org.apache.kafka:kafka-clients:2.8.1",
    "org.apache.logging.log4j:log4j-api:2.17.1",
    "org.apache.logging.log4j:log4j-to-slf4j:2.17.1",
    "org.apache.lucene:lucene-analyzers-common:8.11.1",
    "org.apache.lucene:lucene-backward-codecs:8.11.1",
    "org.apache.lucene:lucene-core:8.11.1",
    "org.apache.lucene:lucene-grouping:8.11.1",
    "org.apache.lucene:lucene-highlighter:8.11.1",
    "org.apache.lucene:lucene-join:8.11.1",
    "org.apache.lucene:lucene-memory:8.11.1",
    "org.apache.lucene:lucene-misc:8.11.1",
    "org.apache.lucene:lucene-queries:8.11.1",
    "org.apache.lucene:lucene-queryparser:8.11.1",
    "org.apache.lucene:lucene-sandbox:8.11.1",
    "org.apache.lucene:lucene-spatial-extras:8.11.1",
    "org.apache.lucene:lucene-spatial3d:8.11.1",
    "org.apache.lucene:lucene-suggest:8.11.1",
    "org.apache.maven.plugin-tools:maven-plugin-annotations:3.4",
    "org.apache.maven.plugin-tools:maven-plugin-annotations:3.4",
    "org.apache.maven:maven-plugin-api:3.6.3",
    "org.apache.maven:maven-plugin-api:3.6.3",
    "org.apache.neethi:neethi:3.1.1",
    "org.apache.santuario:xmlsec:2.2.4",
    "org.apache.sshd:sshd-core:2.9.2",
    "org.apache.sshd:sshd-common:2.9.2",
    "org.apache.sshd:sshd-scp:2.9.2",
    "org.apache.ws.xmlschema:xmlschema-core:2.2.5",
    "org.atmosphere:atmosphere-runtime:2.7.6",
    "org.atmosphere:wasync:3.0.0",
    "org.atteo:evo-inflector:1.2.2",
    "org.bitbucket.b_c:jose4j:0.7.0",
    "org.bouncycastle:bcpg-jdk15on:1.70",
    "org.bouncycastle:bcpkix-jdk15on:1.70",
    "org.bouncycastle:bcprov-ext-jdk15on:1.70",
    "org.bouncycastle:bcprov-jdk15on:1.70",
    "org.checkerframework:checker-compat-qual:2.5.5",
    "org.checkerframework:checker-qual:3.4.0",
    "org.cloudfoundry:cloudfoundry-client-reactor:5.9.0.RELEASE",
    "org.cloudfoundry:cloudfoundry-client:5.9.0.RELEASE",
    "org.cloudfoundry:cloudfoundry-operations:5.9.0.RELEASE",
    "org.cloudfoundry:cloudfoundry-util:5.9.0.RELEASE",
    "org.codehaus.groovy:groovy:3.0.15",
    "org.codehaus.jackson:jackson-core-asl:1.9.11",
    "org.codehaus.janino:commons-compiler:3.0.6",
    "org.codehaus.janino:janino:3.0.6",
    "org.codehaus.mojo:animal-sniffer-annotations:1.18",
    "org.codehaus.plexus:plexus-utils:3.0.20",
    "org.codehaus.woodstox:stax2-api:4.2",
    "org.conscrypt:conscrypt-openjdk-uber:2.5.1",
    "org.coursera:metrics-datadog:1.1.13",
    "org.cryptacular:cryptacular:1.1.4",
    "org.javassist:javassist:3.27.0-GA",
    "org.glassfish:jakarta.el:3.0.4",
    "org.eclipse.jetty.http2:http2-common:9.4.51.v20230217",
    "org.eclipse.jetty.http2:http2-hpack:9.4.51.v20230217",
    "org.eclipse.jetty.http2:http2-server:9.4.51.v20230217",
    "org.eclipse.jetty.toolchain.setuid:jetty-setuid-java:1.0.4",
    "org.eclipse.jetty.websocket:websocket-api:9.4.51.v20230217",
    "org.eclipse.jetty.websocket:websocket-client:9.4.51.v20230217",
    "org.eclipse.jetty.websocket:websocket-common:9.4.51.v20230217",
    "org.eclipse.jetty.websocket:websocket-server:9.4.51.v20230217",
    "org.eclipse.jetty.websocket:websocket-servlet:9.4.51.v20230217",
    "org.eclipse.jetty:jetty-alpn-openjdk8-server:9.4.51.v20230217",
    "org.eclipse.jetty:jetty-alpn-server:9.4.51.v20230217",
    "org.eclipse.jetty:jetty-client:9.4.51.v20230217",
    "org.eclipse.jetty:jetty-continuation:9.4.51.v20230217",
    "org.eclipse.jetty:jetty-http:9.4.51.v20230217",
    "org.eclipse.jetty:jetty-io:9.4.51.v20230217",
    "org.eclipse.jetty:jetty-security:9.4.51.v20230217",
    "org.eclipse.jetty:jetty-server:9.4.51.v20230217",
    "org.eclipse.jetty:jetty-servlet:9.4.51.v20230217",
    "org.eclipse.jetty:jetty-servlets:9.4.51.v20230217",
    "org.eclipse.jetty:jetty-util:9.4.51.v20230217",
    "org.eclipse.jetty:jetty-webapp:9.4.51.v20230217",
    "org.eclipse.jetty:jetty-xml:9.4.51.v20230217",
    "org.eclipse.jgit:org.eclipse.jgit.ssh.jsch:jar:6.5.0.202303070854-r",
    "org.eclipse.jgit:org.eclipse.jgit.http.apache:6.5.0.202303070854-r",
    "org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r",
    "org.elasticsearch.client:elasticsearch-rest-client:7.17.7",
    "org.elasticsearch.client:elasticsearch-rest-high-level-client:7.17.7",
    "org.elasticsearch.plugin:aggs-matrix-stats-client:7.17.7",
    "org.elasticsearch.plugin:lang-mustache-client:7.17.7",
    "org.elasticsearch.plugin:mapper-extras-client:7.17.7",
    "org.elasticsearch.plugin:parent-join-client:7.17.7",
    "org.elasticsearch.plugin:rank-eval-client:7.17.7",
    "org.elasticsearch:elasticsearch-cli:7.17.7",
    "org.elasticsearch:elasticsearch-core:7.17.7",
    "org.elasticsearch:elasticsearch-geo:7.17.7",
    "org.elasticsearch:elasticsearch-secure-sm:7.17.7",
    "org.elasticsearch:elasticsearch-x-content:7.17.7",
    "org.elasticsearch:elasticsearch:7.17.7",
    "org.elasticsearch:jna:5.5.0",
    "org.freemarker:freemarker:2.3.28",
    "org.glassfish.corba:glassfish-corba-orb:4.2.4",
    "org.glassfish.hk2.external:aopalliance-repackaged:2.6.1",
    "org.glassfish.hk2.external:jakarta.inject:2.6.1",
    "org.glassfish.hk2.external:javax.inject:2.5.0-b32",
    "org.glassfish.hk2:hk2-api:2.6.1",
    "org.glassfish.hk2:hk2-locator:2.5.0-b32",
    "org.glassfish.hk2:hk2-utils:2.6.1",
    "org.glassfish.hk2:osgi-resource-locator:1.0.3",
    "org.glassfish.jaxb:jaxb-runtime:2.3.2",
    "org.glassfish.jaxb:txw2:2.3.2",
    "org.glassfish.jersey.bundles.repackaged:jersey-guava:2.26-b03",
    "org.glassfish.jersey.containers:jersey-container-servlet-core:2.34",
    "org.glassfish.jersey.containers:jersey-container-servlet:2.34",
    "org.glassfish.jersey.core:jersey-client:2.34",
    "org.glassfish.jersey.core:jersey-common:2.34",
    "org.glassfish.jersey.core:jersey-server:2.34",
    "org.glassfish.jersey.ext:jersey-bean-validation:2.34",
    "org.glassfish.jersey.ext:jersey-metainf-services:2.34",
    "org.glassfish.jersey.inject:jersey-hk2:2.34",
    "org.glassfish.jersey.media:jersey-media-jaxb:2.34",
    "org.glassfish.jersey.media:jersey-media-multipart:2.34",
    "org.glassfish:javax.el:3.0.0",
    "org.hamcrest:hamcrest-all:1.3",
    "org.hamcrest:hamcrest-core:1.3",
    "org.hdrhistogram:HdrHistogram:2.1.9",
    "org.hibernate:hibernate-validator:6.1.5.Final",
    "org.jacoco:org.jacoco.agent:0.8.5",
    "org.jacorb:jacorb-omgapi:3.9",
    "org.jboss.aerogear:aerogear-otp-java:1.0.0",
    "org.jboss.aerogear:aerogear-otp-java:1.0.0",
    "org.jboss.logging:jboss-logging:3.3.0.Final",
    "org.jboss.marshalling:jboss-marshalling-river:2.0.9.Final",
    "org.jboss.marshalling:jboss-marshalling:2.0.9.Final",
    "org.jboss.spec.javax.rmi:jboss-rmi-api_1.0_spec:1.0.6.Final",
    "org.jetbrains.kotlin:kotlin-stdlib-common:1.7.20",
    "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.7.20",
    "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.20",
    "org.jetbrains.kotlin:kotlin-stdlib:1.7.20",
    "org.jetbrains:annotations:13.0",
    "org.jfree:jfreechart:1.5.0",
    "org.jfrog.artifactory.client:artifactory-java-client-api:2.13.1",
    "org.jfrog.artifactory.client:artifactory-java-client-httpClient:2.13.1",
    "org.jfrog.artifactory.client:artifactory-java-client-services:2.13.1",
    "org.joda:joda-convert:1.2",
    "org.jodd:jodd-bean:5.0.13",
    "org.jodd:jodd-core:5.0.13",
    "org.jooq:jooq:3.14.8",
    "org.jooq:joor:0.9.5",
    "org.json:json:20230227",
    "org.jsoup:jsoup:1.8.3",
    "org.jsoup:jsoup:1.8.3",
    "org.jsr107.ri:cache-annotations-ri-common:1.0.0",
    "org.jsr107.ri:cache-annotations-ri-guice:1.0.0",
    "org.jvnet.mimepull:mimepull:1.9.6",
    "org.jvnet.staxex:stax-ex:1.8.1",
    "org.ldaptive:ldaptive:1.2.3",
    "org.lz4:lz4-java:1.6.0",
    "org.lz4:lz4-java:1.6.0",
    "org.mindrot:jbcrypt:0.4",
    "org.modelmapper:modelmapper:0.7.5",
    "dev.morphia.morphia:core:1.6.1",
    "org.mongodb:mongodb-driver-legacy:4.6.1",
    "org.mongodb:mongodb-driver-sync:4.6.1",
    "org.mongodb:mongodb-driver-core:4.6.1",
    "org.mongodb:bson:4.6.1",
    "org.mortbay.jetty.alpn:alpn-boot:8.1.13.v20181017",
    "org.mozilla:rhino:1.7R4",
    "org.objenesis:objenesis:2.6",
    "org.opensaml:opensaml-core:3.4.3",
    "org.opensaml:opensaml-messaging-api:3.4.3",
    "org.opensaml:opensaml-profile-api:3.4.3",
    "org.opensaml:opensaml-saml-api:3.4.3",
    "org.opensaml:opensaml-security-api:3.4.3",
    "org.opensaml:opensaml-security-impl:3.4.3",
    "org.opensaml:opensaml-soap-api:3.4.3",
    "org.opensaml:opensaml-soap-impl:3.4.3",
    "org.opensaml:opensaml-storage-api:3.4.3",
    "org.opensaml:opensaml-xmlsec-api:3.4.3",
    "org.opensaml:opensaml-xmlsec-impl:3.4.3",
    "org.ow2.asm:asm-analysis:5.0.4",
    "org.ow2.asm:asm-commons:5.0.4",
    "org.ow2.asm:asm-tree:5.0.4",
    "org.ow2.asm:asm-util:5.0.4",
    "org.ow2.asm:asm:5.0.4",
    "org.passay:passay:1.3.1",
    "org.postgresql:postgresql:42.2.25",
    "org.projectlombok:lombok:1.18.26",
    "org.quartz-scheduler:quartz:2.3.2",
    "org.reactivestreams:reactive-streams:1.0.2",
    "org.redisson:redisson:3.17.7",
    "org.reflections:reflections:0.9.12-SNAPSHOT",
    "org.slf4j:jcl-over-slf4j:1.7.30",
    "org.slf4j:jul-to-slf4j:1.7.30",
    "org.slf4j:log4j-over-slf4j:1.7.30",
    "org.slf4j:slf4j-api:1.7.30",
    "org.sonatype.nexus.plugins:nexus-restlet1x-model:2.14.18-01",
    "org.springframework.batch:spring-batch-core:4.2.2.RELEASE",
    "org.springframework.boot:spring-boot-autoconfigure:2.7.10",
    "org.springframework.boot:spring-boot-loader:2.7.10",
    "org.springframework.boot:spring-boot-starter-batch:2.7.10",
    "org.springframework.boot:spring-boot:2.7.10",
    "org.springframework.data:spring-data-commons:2.7.7",
    "org.springframework.data:spring-data-mongodb:3.4.7",
    "org.springframework.guice:spring-guice:1.1.3.RELEASE",
    "org.springframework.kafka:spring-kafka:2.3.7.RELEASE",
    "org.springframework.retry:spring-retry:1.2.5.RELEASE",
    "org.springframework.security:spring-security-crypto:5.3.5.RELEASE",
    "org.springframework:spring-aop:5.3.27",
    "org.springframework:spring-beans:5.3.27",
    "org.springframework:spring-context:5.3.27",
    "org.springframework:spring-core:5.3.27",
    "org.springframework:spring-expression:5.3.27",
    "org.springframework:spring-jcl:5.3.27",
    "org.springframework:spring-messaging:5.3.27",
    "org.springframework:spring-test:5.3.27",
    "org.springframework:spring-tx:5.3.27",
    "org.springframework:spring-web:5.3.27",
    "org.threeten:threetenbp:1.4.1",
    "org.webjars.npm:viz.js-for-graphviz-java:2.1.3",
    "org.xerial.snappy:snappy-java:1.1.7.3",
    "org.xerial.snappy:snappy-java:1.1.7.3",
    "org.xmlunit:xmlunit-core:2.3.0",
    "org.xmlunit:xmlunit-matchers:2.3.0",
    "org.yaml:snakeyaml:2.0",
    "org.zeroturnaround:zt-exec:1.9",
    "ru.vyarus:guice-validator:1.2.0",
    "com.amazonaws:aws-encryption-sdk-java:2.3.3",
    "software.amazon.ion:ion-java:1.0.2",
    "stax:stax-api:1.0.1",
    "stax:stax:1.2.0",
    "wsdl4j:wsdl4j:1.6.3",
    "xerces:xercesImpl:2.12.2",
    "xml-apis:xml-apis:1.4.01",
    "xml-resolver:xml-resolver:1.2",
    "xpp3:xpp3:1.1.3.3",
    "io.swagger.core.v3:swagger-jaxrs2:2.2.0",
    "io.swagger.core.v3:swagger-jaxrs2-servlet-initializer-v2:2.2.0",
    "io.swagger.core.v3:swagger-core:2.2.0",
    "io.swagger.core.v3:swagger-annotations:2.2.0",
    "io.swagger.core.v3:swagger-models:2.2.0",
    "io.swagger.core.v3:swagger-integration:2.2.0",
    "io.opentelemetry:opentelemetry-api:1.18.0",
    "io.harness:smp-license:1.0.11",
    "com.clickhouse:clickhouse-jdbc:0.3.2-patch11",
]

amazon_artifacts = [
    maven.artifact(
        artifact = x,
        exclusions = [
            "commons-logging:commons-logging",
        ],
        group = "com.amazonaws",
        version = "1.12.261",
    )
    for x in [
        "aws-java-sdk-applicationautoscaling",
        "aws-java-sdk-autoscaling",
        "aws-java-sdk-cloudformation",
        "aws-java-sdk-cloudwatch",
        "aws-java-sdk-codecommit",
        "aws-java-sdk-codedeploy",
        "aws-java-sdk-core",
        "aws-java-sdk-costandusagereport",
        "aws-java-sdk-costexplorer",
        "aws-java-sdk-ec2",
        "aws-java-sdk-ecr",
        "aws-java-sdk-ecs",
        "aws-java-sdk-eks",
        "aws-java-sdk-elasticloadbalancing",
        "aws-java-sdk-elasticloadbalancingv2",
        "aws-java-sdk-iam",
        "aws-java-sdk-kms",
        "aws-java-sdk-lambda",
        "aws-java-sdk-marketplaceentitlement",
        "aws-java-sdk-marketplacemeteringservice",
        "aws-java-sdk-organizations",
        "aws-java-sdk-route53",
        "aws-java-sdk-s3",
        "aws-java-sdk-secretsmanager",
        "aws-java-sdk-servicediscovery",
        "aws-java-sdk-sns",
        "aws-java-sdk-sts",
        "jmespath-java",
    ]
]

amazon_v2_artifacts = [
    maven.artifact(
        artifact = x,
        exclusions = [
            "commons-logging:commons-logging",
        ],
        group = "software.amazon.awssdk",
        version = "2.17.220",
    )
    for x in [
        "ecs",
        "applicationautoscaling",
        "aws-core",
        "core",
        "auth",
        "sts",
        "sdk-core",
        "health",
        "elasticloadbalancingv2",
        "lambda",
        "eks",
    ]
]

powermock_artifacts = [
    maven.artifact(
        testonly = True,
        artifact = x,
        group = "org.powermock",
        version = "2.0.2",
    )
    for x in [
        "powermock-api-mockito2",
        "powermock-api-support",
        "powermock-core",
        "powermock-module-junit4",
        "powermock-module-junit4-common",
        "powermock-reflect",
    ]
]

maven_install(
    name = "maven",
    artifacts = plain_artifacts + amazon_artifacts + amazon_v2_artifacts + powermock_artifacts + [
        maven.artifact(
            artifact = "netty-transport-native-kqueue",
            classifier = "osx-x86_64",
            group = "io.netty",
            version = "4.1.86.Final",
        ),
        maven.artifact(
            artifact = "jjschema",
            exclusions = [
                "javax.ws.rs:jsr311-api",
            ],
            group = "com.github.reinert",
            version = "1.0",
        ),
        maven.artifact(
            artifact = "google-api-services-container",
            exclusions = [
                "com.google.guava:guava-jdk5",
            ],
            group = "com.google.apis",
            version = "v1-rev48-1.23.0",
        ),
        maven.artifact(
            artifact = "clojure",
            exclusions = [
                "org.clojure:clojure",
            ],
            group = "org.clojure",
            version = "1.9.0",
        ),
        maven.artifact(
            artifact = "core.specs.alpha",
            exclusions = [
                "org.clojure:clojure",
            ],
            group = "org.clojure",
            version = "0.1.24",
        ),
        maven.artifact(
            artifact = "spec.alpha",
            exclusions = [
                "org.clojure:clojure",
            ],
            group = "org.clojure",
            version = "0.1.143",
        ),
        maven.artifact(
            artifact = "debezium-embedded",
            exclusions = [
                "log4j:log4j",
                "org.slf4j:slf4j-log4j12",
            ],
            group = "io.debezium",
            version = "1.7.2.Final",
        ),
        maven.artifact(
            artifact = "connect-runtime",
            exclusions = [
                "log4j:log4j",
                "org.slf4j:slf4j-log4j12",
            ],
            group = "org.apache.kafka",
            version = "2.8.1",
        ),
        maven.artifact(
            artifact = "opensaml-saml-impl",
            exclusions = [
                "org.apache.velocity:velocity-engine-core",
                "org.apache.velocity:velocity",
            ],
            group = "org.opensaml",
            version = "3.4.3",
        ),
        maven.artifact(
            artifact = "saml-client",
            exclusions = [
                "org.apache.velocity:velocity-engine-core",
                "org.apache.velocity:velocity",
            ],
            group = "com.coveo",
            version = "3.0.2",
        ),
    ],
    excluded_artifacts = [],
    maven_install_json = "//project:main_maven_install.json",
    override_targets = {
        "org.apache.commons:commons-io": "@maven//:commons_io_commons_io",
        "com.jcraft:jsch": "@maven//:com_jcraft_harness_jsch_0_1_54_harness_patch",
        "org.mongodb:mongodb-driver": "@maven//:org_mongodb_mongodb_driver_core",
    },
    repositories = [
        "https://harness-artifactory.harness.io/artifactory/portal-maven",
        "https://harness.jfrog.io/artifactory/harness-internal",
        "https://harness.jfrog.io/harness/thirdparty-annonymous",
    ],
    version_conflict_policy = "pinned",
)

load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()

maven_install(
    name = "ce_nextgen",
    artifacts = [
        "com.graphql-java:graphql-java:16.2",
        "org.antlr:antlr4-runtime:4.8",
    ],
    override_targets = {
        "com.fasterxml.jackson.core:jackson-databind": "@maven//:com_fasterxml_jackson_core_jackson_databind",
    },
    repositories = [
        "https://harness-artifactory.harness.io/artifactory/portal-maven",
        "https://harness.jfrog.io/harness/thirdparty-annonymous",
    ],
    version_conflict_policy = "pinned",
)

maven_install(
    name = "batch",
    artifacts = [
        "com.fasterxml.jackson.core:jackson-databind:2.13.4.2",
        "com.azure:azure-core:1.29.1",
        "com.azure:azure-identity:1.5.0",
        "com.azure:azure-storage-blob:12.16.0",
        "com.azure:azure-storage-common:12.15.1",
    ],
    repositories = [
        "https://harness-artifactory.harness.io/artifactory/portal-maven",
        "https://harness.jfrog.io/harness/thirdparty-annonymous",
    ],
    version_conflict_policy = "pinned",
)

maven_install(
    name = "ce-nextgen",
    artifacts = [
        "com.azure:azure-core:1.29.1",
        "com.azure:azure-core-management:1.6.2",
        "com.azure:azure-identity:1.5.0",
        "com.azure:azure-storage-blob:12.16.0",
        "com.azure:azure-storage-common:12.15.1",
        "com.azure.resourcemanager:azure-resourcemanager:2.14.0",
        "com.azure.resourcemanager:azure-resourcemanager-authorization:2.14.0",
        "com.azure.resourcemanager:azure-resourcemanager-resources:2.14.0",
    ],
    repositories = [
        "https://harness-artifactory.harness.io/artifactory/portal-maven",
        "https://harness.jfrog.io/harness/thirdparty-annonymous",
    ],
    version_conflict_policy = "pinned",
)

maven_install(
    name = "maven_checkstyle",
    artifacts = [
        "antlr:antlr:2.7.2",
        "org.antlr:antlr4-runtime:4.7.2",
        "com.puppycrawl.tools:checkstyle:8.18",
        "commons-beanutils:commons-beanutils:1.9.2",
        "info.picocli:picocli:3.8.2",
        "commons-collections:commons-collections:3.2.2",
        "com.google.guava:guava:21.0",
        "org.slf4j:slf4j-simple:1.7.29",
        "org.slf4j:jcl-over-slf4j:1.7.29",
        "com.github.sevntu-checkstyle:sevntu-checks:1.35.0",
    ],
    repositories = [
        "https://harness-artifactory.harness.io/artifactory/portal-maven",
        "https://harness.jfrog.io/harness/thirdparty-annonymous",
    ],
)

# Adding maven rule for upgraded version of debezium (2.1.3.Final) and required version of mongodb java driver for debezium service
maven_install(
    name = "maven_debezium",
    artifacts = [
        "org.mongodb:mongodb-driver-core:4.0.4",
        "org.mongodb:mongodb-driver-sync:4.0.4",
        "io.debezium:debezium-api:2.1.3.Final",
        "io.debezium:debezium-connector-mongodb:2.1.3.Final",
        "io.debezium:debezium-core:2.1.3.Final",
        maven.artifact(
            artifact = "debezium-embedded",
            exclusions = [
                "log4j:log4j",
                "org.slf4j:slf4j-log4j12",
            ],
            group = "io.debezium",
            version = "2.1.3.Final",
        ),
    ],
    repositories = [
        "https://harness-artifactory.harness.io/artifactory/portal-maven",
        "https://harness.jfrog.io/harness/thirdparty-annonymous",
    ],
)

http_archive(
    name = "openjdk8u242_linux_archive",
    build_file_content = """
java_runtime(name = 'runtime', srcs =  glob(['**']), java='//:bin/java', visibility = ['//visibility:public'])
exports_files(["WORKSPACE"], visibility = ["//visibility:public"])
""",
    sha256 = "f39b523c724d0e0047d238eb2bb17a9565a60574cf651206c867ee5fc000ab43",
    strip_prefix = "jdk8u242-b08",
    urls = ["https://harness-artifactory.harness.io/artifactory/adoptjdk8u242-b08-github/download/jdk8u242-b08/OpenJDK8U-jdk_x64_linux_hotspot_8u242b08.tar.gz"],
)

http_archive(
    name = "openjdk8u242_macos_archive",
    build_file_content = """
package(default_visibility = ["//visibility:public"])
java_runtime(
    name = 'runtime',
    srcs =  glob(['**']),
    java='//:Contents/Home/bin/java'
)
""",
    sha256 = "06675b7d65bce0313ee1f2e888dd44267e8afeced75e0b39b5ad1f5fdff54e0b",
    strip_prefix = "jdk8u242-b08",
    urls = ["https://harness-artifactory.harness.io/artifactory/adoptjdk8u242-b08-github/download/jdk8u242-b08/OpenJDK8U-jdk_x64_mac_hotspot_8u242b08.tar.gz"],
)

#========== Python Configuration Begin=========================

# Special logic for building python interpreter with OpenSSL from homebrew.
# See https://devguide.python.org/setup/#macos-and-os-x
_py_configure = """
if [[ "$OSTYPE" == "darwin"* ]]; then
    ./configure --prefix=$(pwd)/bazel_install --with-openssl=$(brew --prefix openssl)
else
    ./configure --prefix=$(pwd)/bazel_install
fi
"""

http_archive(
    name = "python_interpreter",
    build_file_content = """
exports_files(["python_bin"])
filegroup(
    name = "files",
    srcs = glob(["bazel_install/**"], exclude = ["**/* *"]),
    visibility = ["//visibility:public"],
)
""",
    patch_cmds = [
        "mkdir $(pwd)/bazel_install",
        _py_configure,
        "make",
        "make install",
        "ln -s bazel_install/bin/python3 python_bin",
    ],
    sha256 = "0a8fbfb5287ebc3a13e9baf3d54e08fa06778ffeccf6311aef821bb3a6586cc8",
    strip_prefix = "Python-3.9.10",
    urls = ["https://www.python.org/ftp/python/3.9.10/Python-3.9.10.tar.xz"],
)

#register_toolchains(
#    "//:harness_no_fdLimit_jdk11_toolchain_definition",
#    "//:harness_no_fdLimit_jdk17_toolchain_definition",
#)

#register_toolchains("//:py_toolchain")

#========== Docker Rules Configuration Begin=========================

http_archive(
    name = "io_bazel_rules_docker",
    sha256 = "59d5b42ac315e7eadffa944e86e90c2990110a1c8075f1cd145f487e999d22b3",
    strip_prefix = "rules_docker-0.17.0",
    urls = ["https://harness-artifactory.harness.io/artifactory/rules-docker-github/download/v0.17.0/rules_docker-v0.17.0.tar.gz"],
)

load(
    "@io_bazel_rules_docker//repositories:repositories.bzl",
    container_repositories = "repositories",
)

container_repositories()

load(
    "@io_bazel_rules_docker//container:container.bzl",
    "container_pull",
)

#TO-DO: Build the safe image in bazel only and use it as base.
container_pull(
    name = "platform_alpine",
    digest = "sha256:a568e8f557c055ce59215ccdb864e8a73ac7ff9deed260ae9ced82f0a86e42bb",
    registry = "us.gcr.io",
    repository = "platform-205701/alpine",
    tag = "safe-alpine3.12-sec1338-apm",
)

container_pull(
    name = "platform_ubuntu",
    digest = "sha256:8540a3afd5c6d43a9f6549f19f56abff42c7010265426c3c39ccc64d1d88a1c2",
    registry = "us.gcr.io",
    repository = "platform-205701/ubuntu",
    tag = "safe-ubuntu18.04-sec1338",
)

container_pull(
    name = "openjdk_8u242",
    registry = "index.docker.io",
    repository = "adoptopenjdk/openjdk8",
    tag = "x86_64-alpine-jdk8u242-b08",
)

container_pull(
    name = "nginx",
    registry = "index.docker.io",
    repository = "nginx",
    tag = "latest",
)

load(
    "@io_bazel_rules_docker//repositories:repositories.bzl",
    container_repositories = "repositories",
)

container_repositories()

#========== Docker Rules Configuration End=========================

http_archive(
    name = "rules_pkg",
    sha256 = "038f1caa773a7e35b3663865ffb003169c6a71dc995e39bf4815792f385d837d",
    urls = [
        "https://harness-artifactory.harness.io/artifactory/rules-pkg-github/download/0.4.0/rules_pkg-0.4.0.tar.gz",
        #"https://mirror.bazel.build/github.com/bazelbuild/rules_pkg/releases/download/0.4.0/rules_pkg-0.4.0.tar.gz",
        #"https://github.com/bazelbuild/rules_pkg/releases/download/0.4.0/rules_pkg-0.4.0.tar.gz",
    ],
)

load("@rules_pkg//:deps.bzl", "rules_pkg_dependencies")

rules_pkg_dependencies()

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_file")

http_archive(
    name = "jre_x64_linux_8u242b08",
    build_file_content = """
load("@rules_pkg//:pkg.bzl", "pkg_tar")

pkg_tar(
    name = "jre_x64_linux_8u242b08",
    package_dir = "/opt/harness-delegate",
    srcs = glob(["jdk8u242-b08-jre/**"]),
    strip_prefix = '.',
    visibility = ["//visibility:public"],
)
""",
    sha256 = "5edfaefdbb0469d8b24d61c8aef80c076611053b1738029c0232b9a632fe2708",
    urls = ["https://app.harness.io/storage/wingsdelegates/jre/openjdk-8u242/jre_x64_linux_8u242b08.tar.gz"],
)

http_file(
    name = "alpn_boot_8.1.13.v20181017",
    sha256 = "05165e53fd9aeb774f95178c85740c3ee9ea72a9ca489497df837cc397a5da06",
    urls = ["https://app.harness.io/public/shared/tools/alpn/release/8.1.13.v20181017/alpn-boot-8.1.13.v20181017.jar"],
)

go_repository(
    name = "com_github_go_co_op_gocron",
    importpath = "github.com/go-co-op/gocron",
    sum = "h1:gkWhY/CLtDdVXTquqGtBthig0sdHGTBOyo+kR36Pglw=",
    version = "v1.7.0",
)

go_repository(
    name = "com_github_robfig_cron_v3",
    importpath = "github.com/robfig/cron/v3",
    sum = "h1:WdRxkvbJztn8LMz/QEvLN5sBU+xKpSqwwUO1Pjr4qDs=",
    version = "v3.0.1",
)

go_repository(
    name = "com_github_robinjoseph08_redisqueue_v2",
    importpath = "github.com/robinjoseph08/redisqueue/v2",
    sum = "h1:GactHlrxS8YSCJc4CbP1KbTObo14pieNmNWSUlquTGI=",
    version = "v2.1.0",
)

go_repository(
    name = "com_github_bmatcuk_doublestar",
    importpath = "github.com/bmatcuk/doublestar",
    sum = "h1:1jLE2y0VpSrOn/QR9G4f2RmrCtkM3AuATcWradjHUvM=",
    version = "v1.3.0",
)

go_repository(
    name = "com_github_wings_software_autogen_go",
    importpath = "github.com/wings-software/autogen-go",
    sum = "h1:Ovgmeh04oFhEFg+LZy9c4Yo/Yv9Lkl0rFYFJ7jlhtN4=",
    version = "v1.0.0",
)

go_repository(
    name = "com_github_drone_spec",
    importpath = "github.com/drone/spec",
    sum = "h1:SCRvpZOFAS/kFo6q0c+oeH+ID0OBDPLpDAx4uHYQwYo=",
    version = "v0.0.0-20230328162846-c2bc4a6d62ab",
)

go_repository(
    name = "com_github_99designs_httpsignatures_go",
    importpath = "github.com/99designs/httpsignatures-go",
    sum = "h1:Xa6lInWHNQnuWoF0YPSsx+INFA9qk7/7pTjwb3PInkY=",
    version = "v0.0.0-20170731043157-88528bf4ca7e",
)

go_repository(
    name = "com_github_drone_go_convert",
    importpath = "github.com/drone/go-convert",
    sum = "h1:ybfe4Po+BUQjm2BpIZjEpng9fXBYgPvAehNe2edC0lY=",
    version = "v0.0.0-20230329091825-a238380a6b0e",
)

# Contrib rules jvm for build cleaner.
http_archive(
    name = "contrib_rules_jvm",
    sha256 = "a939cd04da2deee16131898d91d8e23559dcd1a30a5128beac30a2b01b33c94f",
    strip_prefix = "rules_jvm-0.4.0",
    url = "https://harness-artifactory.harness.io/artifactory/bazel-contrib-rules-jvm/archive/v0.4.0.tar.gz",
)

# Fetches the contrib_rules_jvm dependencies.
# If you want to have a different version of some dependency,
# you should fetch it *before* calling this.
load("@contrib_rules_jvm//:repositories.bzl", "contrib_rules_jvm_deps", "contrib_rules_jvm_gazelle_deps")

contrib_rules_jvm_deps()

contrib_rules_jvm_gazelle_deps()

# Now ensure that the downloaded deps are properly configured
load("@contrib_rules_jvm//:setup.bzl", "contrib_rules_jvm_setup")

contrib_rules_jvm_setup()

load("@contrib_rules_jvm//:gazelle_setup.bzl", "contib_rules_jvm_gazelle_setup")

contib_rules_jvm_gazelle_setup()
